package com.mercadolibro.service.impl;

import com.mercadolibro.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void delete(Long id) throws Exception {
        if (!bookRepository.existsById(id)) {
            throw new Exception("Book not found");
        }

        bookRepository.deleteById(id);
    }

}
