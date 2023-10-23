package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.entities.Book;
import com.mercadolibro.exceptions.NoBooksToShowException;
import com.mercadolibro.exceptions.BookNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    private ObjectMapper mapper;

    private static final String BOOK_NOT_FOUND_ERROR_FORMAT = "Could not found book with ID #%d.";

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
        Book saved = bookRepository.save(mapper.convertValue(book, Book.class));
        return mapper.convertValue(saved, BookRespDTO.class);
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
    public BookRespDTO findByID(Long id) {
        Optional<Book> searched = bookRepository.findById(id);
        if (searched.isPresent()) {
            return mapper.convertValue(searched.get(), BookRespDTO.class);
        }
        throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
    }
  
      public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, id));
        }
    }
}
