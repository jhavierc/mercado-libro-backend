package com.mercadolibro.service;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {
    List<BookRespDTO> findAll();
    BookRespDTO save(BookReqDTO book);
    List<BookRespDTO> findAllByCategory(String category);
}
