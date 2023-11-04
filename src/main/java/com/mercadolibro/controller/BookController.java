package com.mercadolibro.controller;

import com.mercadolibro.dto.annotation.BookRequest;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookReqPatchDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/save")
    public ResponseEntity<BookRespDTO> save(@BookRequest @Valid BookReqDTO book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookRespDTO>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRespDTO> findByID(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findByID(id));
    }

    @GetMapping("/all/{category}")
    public ResponseEntity<List<BookRespDTO>> findAllByCategory(@PathVariable String category) {
        return ResponseEntity.ok(bookService.findAllByCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @BookRequest @Valid BookReqDTO bookReqDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(id, bookReqDTO));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable Long id, @BookRequest @Valid BookReqPatchDTO bookReqPatchDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.patch(id, bookReqPatchDTO));
    }
}
