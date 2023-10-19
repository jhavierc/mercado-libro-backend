package com.mercadolibro.service;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;

import java.util.List;

public interface BookService {
    List<BookRespDTO> getAll();

    BookRespDTO getByID(Long id);

    BookRespDTO getByISBN(String isbn);

    BookRespDTO create(BookReqDTO bookReqDTO);

    BookRespDTO update(Long id, BookReqDTO bookReqDTO);

    void delete(Long id);
}
