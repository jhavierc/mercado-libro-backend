package com.mercadolibro.controller;

import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT;
import static com.mercadolibro.service.impl.BookServiceImpl.NOT_FOUND_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService);
    }

    @Test
    public void testUpdateBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO input = new BookReqDTO();
        BookRespDTO output = new BookRespDTO();

        when(bookService.update(bookId, input)).thenReturn(output);

        // Act
        ResponseEntity<BookRespDTO> response = bookController.update(bookId, input);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(output, response.getBody());
        verify(bookService, Mockito.times(1)).update(bookId, input);
    }

    @Test
    public void testUpdateNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO input = new BookReqDTO();
        String expectedErrorMessage = String.format(NOT_FOUND_ERROR_FORMAT, "book", bookId);

        when(bookService.update(bookId, input)).thenThrow(new BookNotFoundException(expectedErrorMessage));

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookController.update(bookId, input));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testUpdateNonExistingBookISBN() {
        // Arrange
        Long bookId = 1L;
        String bookIsbn = "978-0-261-10238-4";
        BookReqDTO input = new BookReqDTO();
        input.setIsbn(bookIsbn);
        String expectedErrorMessage = String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookIsbn);

        when(bookService.update(bookId, input)).thenThrow(new BookAlreadyExistsException(expectedErrorMessage));

        // Act and Assert
        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class,
                () -> bookController.update(bookId, input));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testPatchBook() {
        // Arrange
        Long bookId = 1L;
        BookRespDTO input = new BookRespDTO();
        BookRespDTO output = new BookRespDTO();

        when(bookService.patch(bookId, input)).thenReturn(output);

        // Act
        ResponseEntity<BookRespDTO> response = bookController.patch(bookId, input);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(output, response.getBody());
        verify(bookService, Mockito.times(1)).patch(bookId, input);
    }

    @Test
    public void testPatchNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookRespDTO input = new BookRespDTO();
        String expectedErrorMessage = String.format(NOT_FOUND_ERROR_FORMAT, "book", bookId);

        when(bookService.patch(bookId, input)).thenThrow(new BookNotFoundException(expectedErrorMessage));

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookController.patch(bookId, input));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testPatchExistingBookISBN() {
        // Arrange
        Long bookId = 1L;
        String bookIsbn = "978-0-261-10238-4";
        BookRespDTO input = new BookRespDTO();
        input.setIsbn(bookIsbn);
        String expectedErrorMessage = String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookIsbn);

        when(bookService.patch(bookId, input)).thenThrow(new BookAlreadyExistsException(expectedErrorMessage));

        // Act and Assert
        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class,
                () -> bookController.patch(bookId, input));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testDeleteBook() {
        // Arrange
        Long bookId = 1L;
        doNothing().when(bookService).delete(bookId);

        // Act
        ResponseEntity<Object> response = bookController.delete(bookId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService, Mockito.times(1)).delete(bookId);
    }

    @Test
    public void testDeleteNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        String expectedErrorMessage = String.format(NOT_FOUND_ERROR_FORMAT, "book", bookId);

        doThrow(new BookNotFoundException(expectedErrorMessage)).when(bookService).delete(bookId);

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookController.delete(bookId));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
