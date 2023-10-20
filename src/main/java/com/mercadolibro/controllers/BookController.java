package com.mercadolibro.controllers;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody BookReqDTO bookReqDTO) {
        try{
            BookRespDTO bookRespDTO = bookService.update(id, bookReqDTO);
            return ResponseEntity.status(HttpStatus.OK).body(bookRespDTO);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable("id") Long id, @RequestBody BookReqDTO bookReqDTO) {
        try{
            BookRespDTO bookRespDTO = bookService.patch(id, bookReqDTO);
            return ResponseEntity.status(HttpStatus.OK).body(bookRespDTO);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
