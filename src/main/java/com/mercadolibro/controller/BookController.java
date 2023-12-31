package com.mercadolibro.controller;

import com.mercadolibro.dto.*;
import com.mercadolibro.dto.annotation.BookRequest;
import com.mercadolibro.service.BookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/book")
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
    public ResponseEntity<BookRespDTO> save(@RequestBody  @Valid BookReqDTO book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
    }

    @GetMapping
    @ApiOperation(value = "Get books", notes = "Returns books, whether filtered, ordered, or all")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Books found successfully", response = BookRespDTO.class),
                    @ApiResponse(code = 404, message = "No books to show"),
                    @ApiResponse(code = 400, message = "Page less than or equal to zero", response = Map.class)
            }
    )
    public ResponseEntity<PageDTO<BookRespDTO>> findAll(
            @RequestParam(required = false)
            @Size(min = 1)
            @ApiParam(
                    name = "keyword",
                    type = "String",
                    value = "keyword to search in title, description, authors or publisher",
                    example = "Harry Potter")
                    String keyword,

            @RequestParam(required = false)
            @Size(min = 3)
            @ApiParam(
                    name = "category",
                    type = "String",
                    value = "category by which it is filtered",
                    example = "Fiction")
                    String category,

            @RequestParam(required = false)
            @Size(min = 3)
            @ApiParam(
                    name = "publisher",
                    type = "String",
                    value = "publisher by which it is filtered",
                    example = "HarperCollins")
                    String publisher,

            @RequestParam(required = false)
            @ApiParam(
                    name = "releases",
                    type = "boolean",
                    value = "to obtain latest releases",
                    example = "true")
                    boolean releases,

            @RequestParam(required = false)
            @Size(min = 3)
            @ApiParam(
                    name = "selection",
                    type = "String",
                    value = "selection type",
                    example = "newer",
                    allowableValues = "newer,older")
            @Pattern(regexp = "newer|older", message = "selection value must be 'newer' or 'older'")
                    String selection,

            @RequestParam(required = false)
            @Size(min = 3)
            @ApiParam(
                    name = "sort",
                    type = "String",
                    value = "sort type",
                    example = "asc",
                    allowableValues = "asc,desc")
            @Pattern(regexp = "asc|desc", message = "sort value must be 'asc' or 'desc'")
                    String sort,

            @RequestParam
            @PositiveOrZero
            @ApiParam(
                    name = "page",
                    type = "short",
                    value = "page number (starts at zero)",
                    example = "0",
                    required = true)
                    short page,
            @RequestParam
            @PositiveOrZero
            @ApiParam(
                    name = "size",
                    type = "short",
                    value = "size for page",
                    example = "0",
                    required = true)
            short size
    ) {
        return ResponseEntity.ok(bookService.findAll(keyword, category, publisher, releases, selection, sort, page, size));
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
    public ResponseEntity<BookRespDTO> findByID(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(bookService.findByID(id));
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
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
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
    public ResponseEntity<BookRespDTO> update(@PathVariable @Positive Long id, @RequestBody BookReqDTO bookReqDTO) {
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
    public ResponseEntity<BookRespDTO> patch(@PathVariable @Positive Long id, @BookRequest @Valid BookReqPatchDTO bookReqPatchDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.patch(id, bookReqPatchDTO));
    }

    @GetMapping("/author/book-count")
    @ApiOperation(value = "Get total book count by author", notes = "Returns a paginated list of total book count for each author.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author book count retrieved successfully", response = PageDTO.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<PageDTO<AuthorBookCountDTO>> getAuthorBookCount(@RequestParam(defaultValue = "0") @PositiveOrZero int page, @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(bookService.getAllAuthorsBookCount(page, size));
    }
}