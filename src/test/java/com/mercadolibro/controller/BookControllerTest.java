package com.mercadolibro.controller;

import com.mercadolibro.controllers.BookController;
import com.mercadolibro.exceptions.BookNotFoundException;
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

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_NOT_FOUND_ERROR_FORMAT;
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
        ResponseEntity<Object> response = bookController.update(bookId, input);

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
        String expectedErrorMessage = String.format(BOOK_NOT_FOUND_ERROR_FORMAT, bookId);

        when(bookService.update(bookId, input)).thenThrow(new BookNotFoundException(expectedErrorMessage));

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
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
        ResponseEntity<Object> response = bookController.patch(bookId, input);

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
        String expectedErrorMessage = String.format(BOOK_NOT_FOUND_ERROR_FORMAT, bookId);

        when(bookService.patch(bookId, input)).thenThrow(new BookNotFoundException(expectedErrorMessage));

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
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
        String expectedErrorMessage = String.format(BOOK_NOT_FOUND_ERROR_FORMAT, bookId);

        doThrow(new BookNotFoundException(expectedErrorMessage)).when(bookService).delete(bookId);

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookController.delete(bookId));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
