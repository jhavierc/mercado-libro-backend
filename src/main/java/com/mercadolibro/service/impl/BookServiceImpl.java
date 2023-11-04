package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.Book;
import com.mercadolibro.exception.*;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.CategoryRepository;
import com.mercadolibro.service.BookService;
import com.mercadolibro.service.specification.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;

    public static final String NOT_FOUND_ERROR_FORMAT = "Could not found %s with ID #%d.";
    public static final String BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT = "Book with ISBN #%s already exists.";

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository,
                           ObjectMapper mapper, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageDTO<BookRespDTO> findAll(String category, String publisher, boolean releases,
                                        String selection, String sort, short page) {

        Specification<Book> spec = buildSpecification(category, publisher, releases);
        Pageable pageable = buildPageable(sort, selection, page);

        Page<Book> res = bookRepository.findAll(spec, pageable);
        List<BookRespDTO> content = res.getContent().stream().map(book ->
                        mapper.convertValue(book, BookRespDTO.class)).collect(Collectors.toList());

        return new PageDTO<BookRespDTO>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber() + 1,
                res.getSize()
        );
    }

    @Override
    public BookRespDTO save(BookReqDTO book) {
        book.getCategories().forEach(category -> {
            Long id = category.getId();
            if (!categoryRepository.existsById(id)) {
                throw new CategoryNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "category", id));
            }
        });

        if (!bookRepository.existsByIsbn(book.getIsbn())) {
            Book saved = bookRepository.save(mapper.convertValue(book, Book.class));
            return mapper.convertValue(saved, BookRespDTO.class);
        }
        throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, book.getIsbn()));
    }

    @Override
    public BookRespDTO update(Long id, BookReqDTO bookReqDTO){
        if (bookRepository.existsById(id)) {
            if (bookRepository.existsByIsbnAndIdNot(bookReqDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT,
                        bookReqDTO.getIsbn()));
            }

            Book book = mapper.convertValue(bookReqDTO, Book.class);
            book.setId(id);
            Book updated = bookRepository.save(book);
            return mapper.convertValue(updated, BookRespDTO.class);
        }

        throw new BookNotFoundException(String.format(NOT_FOUND_ERROR_FORMAT, "book", id));
    }

    @Override
    public BookRespDTO patch(Long id, BookRespDTO bookRespDTO) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            if (bookRepository.existsByIsbnAndIdNot(bookRespDTO.getIsbn(), id)) {
                throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookRespDTO.getIsbn()));
            }

            bookRespDTO.setId(id);
            modelMapper.map(bookRespDTO, optionalBook.get());
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
        return PageRequest.of(page - 1, 9, sorted);
    }

    private Specification<Book> buildSpecification(String category, String publisher, boolean releases) {
        Specification<Book> spec = Specification.where(null);

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
