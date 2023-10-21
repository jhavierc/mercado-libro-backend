package com.mercadolibro.controller;

import com.mercadolibro.controllers.BookController;
import com.mercadolibro.exceptions.ResourceNotFoundException;
import com.mercadolibro.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
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
        String expectedErrorMessage = BOOK_NOT_FOUND;

        doNothing().when(bookService).delete(bookId);
        doThrow(new ResourceNotFoundException(expectedErrorMessage)).when(bookService).delete(bookId);

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookController.delete(bookId));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
