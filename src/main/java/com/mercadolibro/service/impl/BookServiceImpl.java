package com.mercadolibro.service.impl;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.exceptions.ResourceNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    public static final String BOOK_NOT_FOUND = "Book not found";

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookRespDTO> getAll() {
        return null;
    }

    @Override
    public BookRespDTO getByID(Long id) {
        return null;
    }

    @Override
    public BookRespDTO getByISBN(String isbn) {
        return null;
    }

    @Override
    public BookRespDTO create(BookReqDTO bookReqDTO) {
        return null;
    }

    @Override
    public BookRespDTO update(Long id, BookReqDTO bookReqDTO) {
        return null;
    }

    public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(BOOK_NOT_FOUND);
        }
    }

}
