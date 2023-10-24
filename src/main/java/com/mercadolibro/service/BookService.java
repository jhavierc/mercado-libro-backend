package com.mercadolibro.service;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    BookRespDTO update(Long id, BookReqDTO bookReqDTO);
    BookRespDTO patch(Long id, BookRespDTO bookRespDTO);
    List<BookRespDTO> findAll();
    BookRespDTO save(BookReqDTO book);
    List<BookRespDTO> findAllByCategory(String category);
    BookRespDTO findByID(Long id);
    void delete(Long id);
}
