package com.mercadolibro.service;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;

import java.util.List;

public interface BookService {
    BookRespDTO update(Long id, BookReqDTO bookReqDTO);
    BookRespDTO patch(Long id, BookRespDTO bookRespDTO);
    void delete(Long id);
}
