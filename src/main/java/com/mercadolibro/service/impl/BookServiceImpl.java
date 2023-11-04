package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookReqPatchDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.S3ObjectDTO;
import com.mercadolibro.entity.Book;
import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.NoBooksToShowException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.exception.S3Exception;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.BookService;
import com.mercadolibro.service.S3Service;
import com.mercadolibro.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;

    public static final String BOOK_NOT_FOUND_ERROR_FORMAT = "Could not found book with ID #%d.";
    public static final String BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT = "Book with ISBN #%s already exists.";

    public static final String SAVING_BOOK_ERROR_FORMAT = "There was an error saving the book, image upload rolled back successfully";


    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ObjectMapper mapper, ModelMapper modelMapper, S3Service s3Service) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
        this.s3Service = s3Service;
    }

    @Override
    public List<BookRespDTO> findAll() {
        List<Book> searched = bookRepository.findAll();
        if (!searched.isEmpty()) {
            return searched.stream().map(book -> mapper.convertValue(book, BookRespDTO.class))
                    .collect(Collectors.toList());
        }
        throw new NoBooksToShowException();
    }

    @Override
    public BookRespDTO save(BookReqDTO bookReqDTO) {
        if (!bookRepository.existsByIsbn(bookReqDTO.getIsbn())) {
            List<S3ObjectDTO> imagesRespDTOs = s3Service.uploadFiles(bookReqDTO.getImages());
            try {
                Book mappedBook = mapper.convertValue(bookReqDTO, Book.class);
                mappedBook.setImageLinks(S3Util.getS3ObjectsUrls(imagesRespDTOs));

                Book saved = bookRepository.save(mappedBook);
                return mapper.convertValue(saved, BookRespDTO.class);
            }catch (Exception e){
                s3Service.deleteFiles(imagesRespDTOs); // Rollback image upload
                throw new S3Exception(SAVING_BOOK_ERROR_FORMAT);
            }
        }
        throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookReqDTO.getIsbn()));
    }

    @Override
    public List<BookRespDTO> findAllByCategory(String category) {
        List<Book> searched = bookRepository.findAllByCategory(category);
        if (!searched.isEmpty()) {
            return searched.stream().map(book -> mapper.convertValue(book, BookRespDTO.class))
                    .collect(Collectors.toList());
        }
        throw new NoBooksToShowException();
    }

    @Override
    public BookRespDTO update(Long id, BookReqDTO bookReqDTO){
        if (bookRepository.existsById(id)) {
            if (bookRepository.existsByIsbnAndIdNot(bookReqDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookReqDTO.getIsbn()));
            }

            Book book = mapper.convertValue(bookReqDTO, Book.class);
            book.setId(id);
            Book updated = bookRepository.save(book);
            return mapper.convertValue(updated, BookRespDTO.class);
        }

        throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
    }

    @Override
    public BookRespDTO patch(Long id, BookReqPatchDTO bookReqPatchDTO) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            if (bookRepository.existsByIsbnAndIdNot(bookReqPatchDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookReqPatchDTO.getIsbn()));
            }

            bookReqPatchDTO.setId(id);
            modelMapper.map(bookReqPatchDTO, optionalBook.get());
            Book book = bookRepository.save(optionalBook.get());
            return mapper.convertValue(book, BookRespDTO.class);
        }

        throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
    }

    @Override
    public BookRespDTO findByID(Long id) {
        Optional<Book> searched = bookRepository.findById(id);
        if (searched.isPresent()) {
            return mapper.convertValue(searched.get(), BookRespDTO.class);
        }
        throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
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
            throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
        }
    }
}
