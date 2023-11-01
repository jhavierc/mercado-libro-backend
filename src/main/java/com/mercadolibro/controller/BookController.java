package com.mercadolibro.controller;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.service.BookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@CrossOrigin(origins = "*")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new book", notes = "Returns the created book")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Book created successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 409, message = "Book already exists"),
            }
    )
    public ResponseEntity<BookRespDTO> save(@RequestBody @Valid BookReqDTO book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
    }

    @GetMapping
    @ApiOperation(value = "Get all books", notes = "Returns all books")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Books found successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 404, message = "No books to show")
            }
    )
    public ResponseEntity<List<BookRespDTO>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get book by ID", notes = "Returns a book by ID")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Book found successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 404, message = "Book not found")
            }
    )
    public ResponseEntity<BookRespDTO> findByID(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findByID(id));
    }

    @GetMapping("/category/{name}")
    @ApiOperation(value = "Get book by ID", notes = "Returns all books by category name")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Books found successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 404, message = "No books to show")
            }
    )
    public ResponseEntity<List<BookRespDTO>> findAllByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(bookService.findAllByCategory(categoryName));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete book by ID", notes = "Deletes a book by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "No content", response = void.class),
                    @ApiResponse(code = 404, message = "Book not found")
            }
    )
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates an existing book", notes = "Returns the updated book")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Book updated successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 404, message = "Book not found"),
                    @ApiResponse(code = 409, message = "Book already exists"),
            }
    )
    public ResponseEntity<BookRespDTO> update(@PathVariable Long id, @RequestBody @Valid BookReqDTO bookReqDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(id, bookReqDTO));
    }


    @PatchMapping("/{id}")
    @ApiOperation(value = "Partially updates an existing book", notes = "Returns the updated book")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Book updated successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 404, message = "Book not found"),
                    @ApiResponse(code = 409, message = "Book already exists"),
            }
    )
    public ResponseEntity<BookRespDTO> patch(@PathVariable Long id, @RequestBody @Valid BookRespDTO bookRespDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.patch(id, bookRespDTO));
    }
}
