package com.mercadolibro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.entities.Book;
import com.mercadolibro.exceptions.BookNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_NOT_FOUND_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServicePutPatchDeleteTest {
    @MockBean
    private BookRepository bookRepository;

    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();
        bookService = new BookServiceImpl(bookRepository, objectMapper, modelMapper);
    }

    @Test
    void testUpdateExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO input = new BookReqDTO();
        input.setLanguage("english");

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("old title");
        existingBook.setLanguage("spanish");

        Book mockRepositoryResponse = new Book();
        mockRepositoryResponse.setId(bookId);
        mockRepositoryResponse.setLanguage("english");

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockRepositoryResponse);

        // Act
        BookRespDTO result = bookService.update(bookId, input);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertNull(result.getTitle());
        assertEquals(input.getLanguage(), result.getLanguage());
    }

    @Test
    void testUpdateNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO input = new BookReqDTO();

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookService.update(bookId, input));
        assertEquals(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, bookId), exception.getMessage());
    }

    @Test
    void testPatchExistingBook() {
        // Arrange
        Long bookId = 1L;
        String title = "this title must not be overridden";
        BookRespDTO input = new BookRespDTO();

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle(title);

        Book mockRepositoryResponse = new Book();
        mockRepositoryResponse.setId(bookId);
        mockRepositoryResponse.setTitle(title);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockRepositoryResponse);

        // Act
        BookRespDTO result = bookService.patch(bookId, input);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals(title, result.getTitle());
    }

    @Test
    void testPatchNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookRespDTO input = new BookRespDTO();

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookService.patch(bookId, input));
        assertEquals(String.format(BOOK_NOT_FOUND_ERROR_FORMAT, bookId), exception.getMessage());
        bookService = new BookServiceImpl(bookRepository, new ObjectMapper(), new ModelMapper());
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
