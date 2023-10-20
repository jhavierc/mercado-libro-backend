package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.entities.Book;
import com.mercadolibro.repository.BookRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl {
    private final BookRepository bookRepository;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, ObjectMapper mapper, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public BookRespDTO update(Long id, BookReqDTO bookReqDTO) throws Exception{
        if (!bookRepository.existsById(id)) {
            throw new Exception("Book not found");
        }
        Book updated = bookRepository.save(mapper.convertValue(bookReqDTO, Book.class));

        return mapper.convertValue(updated, BookRespDTO.class);
    }

    public BookRespDTO patch(Long id, BookReqDTO bookReqDTO) throws Exception{
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new Exception("Book not found");
        }

        modelMapper.map(bookReqDTO, optionalBook.get());
        Book book = bookRepository.save(optionalBook.get());

        return mapper.convertValue(book, BookRespDTO.class);
    }
}
