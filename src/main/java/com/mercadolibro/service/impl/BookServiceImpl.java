package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.Category;
import com.mercadolibro.exception.*;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.CategoryRepository;
import com.mercadolibro.service.BookService;
import com.mercadolibro.service.S3Service;
import com.mercadolibro.util.S3Util;
import com.mercadolibro.service.specification.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;

    public static final String NOT_FOUND_ERROR_FORMAT = "Could not found %s with ID #%d.";
    public static final String NOT_FOUND_ERROR_ALL_FORMAT = "Could not found %s.";
    public static final String BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT = "Book with ISBN #%s already exists.";
    public static final String SAVING_BOOK_ERROR_FORMAT = "There was an error saving the book, image upload " +
            "rolled back successfully";
    public static final String FILE_NOT_FOUND_MESSAGE = "The file associated with URL: %s does not exist.";


    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository,
                           ObjectMapper mapper, ModelMapper modelMapper, S3Service s3Service) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
        this.s3Service = s3Service;
    }

    @Override
    public PageDTO<BookRespDTO> findAll(String keyword, String category, String publisher, boolean releases,
                                        String selection, String sort, short page) {

        Specification<Book> spec = buildSpecification(keyword, category, publisher, releases);
        Pageable pageable = buildPageable(sort, selection, page);

        Page<Book> res = bookRepository.findAll(spec, pageable);
        List<BookRespDTO> content = res.getContent().stream().map(book ->
                        mapper.convertValue(book, BookRespDTO.class)).collect(Collectors.toList());

        return new PageDTO<BookRespDTO>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber(),
                res.getSize()
        );
    }

    @Override
    public BookRespDTO save(BookReqDTO bookReqDTO) {
        bookReqDTO.getCategories().forEach(category -> {
            Long id = category.getId();
            if (!categoryRepository.existsById(id)) {
                throw new CategoryNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "category", id));
            }
        });

        if (!bookRepository.existsByIsbn(bookReqDTO.getIsbn())) {
            List<S3ObjectDTO> imagesRespDTOs = s3Service.uploadFiles(bookReqDTO.getImages());
            try {
                Book mappedBook = mapper.convertValue(bookReqDTO, Book.class);
                mappedBook.setImageLinks(S3Util.getS3ObjectsUrls(imagesRespDTOs));

                Book saved = bookRepository.save(mappedBook);
                return mapper.convertValue(saved, BookRespDTO.class);
            } catch (Exception e){
                s3Service.deleteFiles(imagesRespDTOs); // TODO: check if a can do a fallback method instead of repeating rollback with try catch
                throw new S3Exception(SAVING_BOOK_ERROR_FORMAT);
            }
        }
        throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookReqDTO.getIsbn()));
    }

    @Override
    public BookRespDTO update(Long id, BookReqDTO bookReqDTO){
        if (bookRepository.existsById(id)) {
            if (bookRepository.existsByIsbnAndIdNot(bookReqDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT,
                        bookReqDTO.getIsbn()));
            }
            List<S3ObjectDTO> imagesRespDTOs = s3Service.uploadFiles(bookReqDTO.getImages());
            Book mappedBook;

            try {
                mappedBook = mapper.convertValue(bookReqDTO, Book.class);
                mappedBook.setImageLinks(S3Util.getS3ObjectsUrls(imagesRespDTOs));
                mappedBook.setId(id);

                Book updated = bookRepository.save(mappedBook);

                // TODO: At this point, we need to remove old images. However, due to the current backend architecture
                //  and database structure, the logic for this task would be ugly. Creating an images entity
                //  would be necessary to resolve these issues.

                return mapper.convertValue(updated, BookRespDTO.class);
            } catch (Exception e){
                s3Service.deleteFiles(imagesRespDTOs);
                throw new S3Exception(SAVING_BOOK_ERROR_FORMAT);
            }
        }

        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public BookRespDTO patch(Long id, BookReqPatchDTO bookReqPatchDTO) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setCollectionsMergeEnabled(false);

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            if (bookRepository.existsByIsbnAndIdNot(bookReqPatchDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookReqPatchDTO.getIsbn()));
            }

            List<MultipartFile> imagesToUpload = bookReqPatchDTO.getImages();
            List<String> imageURLs = bookReqPatchDTO.getImagesToReplaceURLs();

            String existingImageLinks = bookRepository.findImageLinksById(id);
            List<String> splitLinks = List.of(existingImageLinks.split(","));
            for (String URL : imageURLs) {
                if (!splitLinks.contains(URL)) {
                    throw new BookImageKeyDoesNotExistException(String.format(FILE_NOT_FOUND_MESSAGE, URL));
                }
            }

            s3Service.replaceFilesByURLs(imagesToUpload, imageURLs); // I do not replace new image links in the database because them stays with the same key/url

            HashSet<Category> categoriesOnlyWithID = bookReqPatchDTO.getCategories().stream()
                    .map(categoryDTO -> mapper.convertValue(categoryDTO, Category.class))
                    .collect(Collectors.toCollection(HashSet::new));
            optionalBook.get().setCategories(categoriesOnlyWithID);

            modelMapper.map(bookReqPatchDTO, optionalBook.get());

            Book book = bookRepository.save(optionalBook.get());

            return mapper.convertValue(book, BookRespDTO.class);
        }

        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public BookRespDTO findByID(Long id) {
        Optional<Book> searched = bookRepository.findById(id);
        if (searched.isPresent()) {
            return mapper.convertValue(searched.get(), BookRespDTO.class);
        }
        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            String imageLinks = bookRepository.findImageLinksById(id);
            String[] splitLinks = imageLinks.split(",");

            List<S3ObjectDTO> s3ObjectDTOS = new ArrayList<>();
            for (String link : splitLinks) {
                s3ObjectDTOS.add(S3Util.parseS3Url(link));
            }

            s3Service.deleteFiles(s3ObjectDTOS);
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
        }
    }

    private Pageable buildPageable(String sort, String selection, short page) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sort != null) {
            if (sort.equalsIgnoreCase("desc")) {
                orders.add(Sort.Order.desc("title"));
            } else if (sort.equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc("title"));
            }
        }

        if (selection != null) {
            if (selection.equalsIgnoreCase("newer")) {
                orders.add(Sort.Order.desc("publishedDate"));
            } else if (selection.equalsIgnoreCase("older")) {
                orders.add(Sort.Order.asc("publishedDate"));
            }
        }

        Sort sorted = Sort.by(orders);
        return PageRequest.of(page, 9, sorted);
    }

    private Specification<Book> buildSpecification(String keyword, String category, String publisher, boolean releases) {
        Specification<Book> spec = Specification.where(null);

        if (keyword != null) {
            spec= spec.and(BookSpecification.titleOrAuthorOrPublisherContainingSpec(keyword));
        }

        if (category != null) {
            spec = spec.and(BookSpecification.categorySpec(category));
        }

        if (publisher != null) {
            spec = spec.and(BookSpecification.publisherSpec(publisher));
        }

        if (releases) {
            spec = spec.and(BookSpecification.releasesSpec());
        }

        return spec;
    }
}
