package com.mercadolibro.controller;

import com.mercadolibro.controllers.BookController;
import com.mercadolibro.exceptions.BookNotFoundException;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.exceptions.ResourceNotFoundException;
import com.mercadolibro.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_NOT_FOUND_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookControllerTest {
    @MockBean
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
        BookReqDTO bookReqDTO = new BookReqDTO();
        BookRespDTO bookRespDTO = new BookRespDTO();

        when(bookService.update(bookId, bookReqDTO)).thenReturn(bookRespDTO);

        // Act
        ResponseEntity<Object> response = bookController.update(bookId, bookReqDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookRespDTO, response.getBody());
        verify(bookService, Mockito.times(1)).update(bookId, bookReqDTO);
    }

    @Test
    public void testUpdateNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO bookReqDTO = new BookReqDTO();
        String expectedErrorMessage = BOOK_NOT_FOUND;

        when(bookService.update(bookId, bookReqDTO)).thenThrow(new ResourceNotFoundException(expectedErrorMessage));

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookController.update(bookId, bookReqDTO));
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
        BookRespDTO bookReqDTO = new BookRespDTO();
        String expectedErrorMessage = BOOK_NOT_FOUND;

        when(bookService.patch(bookId, bookReqDTO)).thenThrow(new ResourceNotFoundException(expectedErrorMessage));

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookController.patch(bookId, bookReqDTO));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

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
        String expectedErrorMessage = BOOK_NOT_FOUND_ERROR_FORMAT;

        doNothing().when(bookService).delete(bookId);
        doThrow(new BookNotFoundException(expectedErrorMessage)).when(bookService).delete(bookId);

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> bookController.delete(bookId));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
