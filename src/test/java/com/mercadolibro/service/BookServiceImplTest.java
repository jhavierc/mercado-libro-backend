package com.mercadolibro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.exceptions.BookNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceImplTest {
    @MockBean
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, new ObjectMapper());
    }

    @Test
    void testDeleteExistingBook() {
        // Arrange
        Long bookId = 1L;

        doReturn(true).when(bookRepository).existsById(bookId);
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        bookService.delete(bookId);

        // Assert
        verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteNonExistingBook() {
        // Arrange
        Long bookId = 1L;

        doReturn(false).when(bookRepository).existsById(bookId);

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
    }
}
