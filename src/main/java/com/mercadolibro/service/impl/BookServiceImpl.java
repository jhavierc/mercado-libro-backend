package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.entities.Book;
import com.mercadolibro.exceptions.BookAlreadyExistsException;
import com.mercadolibro.exceptions.NoBooksToShowException;
import com.mercadolibro.exceptions.BookNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;

    public static final String BOOK_NOT_FOUND_ERROR_FORMAT = "Could not found book with ID #%d.";
    public static final String BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT = "Book with ISBN #%s already exists.";

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ObjectMapper mapper, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
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
    public BookRespDTO save(BookReqDTO book) {
        if (!bookRepository.existsByIsbn(book.getIsbn())) {
            Book saved = bookRepository.save(mapper.convertValue(book, Book.class));
            return mapper.convertValue(saved, BookRespDTO.class);
        }
        throw new BookAlreadyExistsException(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, book.getIsbn()));
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
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
        }
    }
}
