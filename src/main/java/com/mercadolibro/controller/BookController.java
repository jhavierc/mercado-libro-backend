package com.mercadolibro.controller;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookReqPatchDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.service.BookService;
import com.mercadolibro.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {
    private final BookService bookService;
    private final S3Service s3Service;

    @Autowired
    public BookController(BookService bookService, S3Service s3Service) {
        this.bookService = bookService;
        this.s3Service = s3Service;
    }

    @PostMapping("/save")
    public ResponseEntity<BookRespDTO> save(@RequestBody @Valid BookReqDTO book) {
        s3Service.uploadFiles(book.getImages());

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
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid BookReqDTO bookReqDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(id, bookReqDTO));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable Long id, @RequestBody @Valid BookReqPatchDTO bookReqPatchDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.patch(id, bookReqPatchDTO));
    }
}
