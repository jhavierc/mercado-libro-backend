package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.entities.Book;
import com.mercadolibro.exceptions.ResourceNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.BookService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    public static final String BOOK_NOT_FOUND = "Book not found";

    private final BookRepository bookRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, ObjectMapper mapper, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
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

    public BookRespDTO update(Long id, BookReqDTO bookReqDTO){
        if (bookRepository.existsById(id)) {
            Book book = mapper.convertValue(bookReqDTO, Book.class);
            book.setId(id);
            Book updated = bookRepository.save(book);
            return mapper.convertValue(updated, BookRespDTO.class);
        }

        throw new ResourceNotFoundException(BOOK_NOT_FOUND);
    }

    public BookRespDTO patch(Long id, BookReqDTO bookReqDTO){
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            modelMapper.map(bookReqDTO, optionalBook.get());
            Book book = bookRepository.save(optionalBook.get());
            return mapper.convertValue(book, BookRespDTO.class);
        }

        throw new ResourceNotFoundException(BOOK_NOT_FOUND);
    }

    @Override
    public void delete(Long id) {

    }
}
