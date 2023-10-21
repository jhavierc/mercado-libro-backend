package com.mercadolibro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.entities.Book;
import com.mercadolibro.exceptions.ResourceNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceImplTest {
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
        BookReqDTO bookReqDTO = new BookReqDTO();
        bookReqDTO.setLanguage("english");

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("old title");
        existingBook.setLanguage("spanish");

        Book mockRepositoryResponse = new Book();
        mockRepositoryResponse.setId(bookId);
        mockRepositoryResponse.setLanguage("english");

        doReturn(true).when(bookRepository).existsById(bookId);
        doReturn(mockRepositoryResponse).when(bookRepository).save(Mockito.any(Book.class));

        // Act
        BookRespDTO result = bookService.update(bookId, bookReqDTO);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertNull(result.getTitle());
        assertEquals(bookReqDTO.getLanguage(), result.getLanguage());
    }

    @Test
    void testUpdateNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO bookReqDTO = new BookReqDTO();

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.update(bookId, bookReqDTO));
    }

    @Test
    void testPatchExistingBook() {
        // Arrange
        Long bookId = 1L;
        String title = "this title must not be overridden";
        BookReqDTO bookReqDTO = new BookReqDTO();

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle(title);

        Book mockRepositoryResponse = new Book();
        mockRepositoryResponse.setId(bookId);
        mockRepositoryResponse.setTitle(title);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doReturn(mockRepositoryResponse).when(bookRepository).save(Mockito.any(Book.class));

        // Act
        BookRespDTO result = bookService.patch(bookId, bookReqDTO);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals(title, result.getTitle());
    }


    @Test
    void testPatchNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO bookReqDTO = new BookReqDTO();

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.patch(bookId, bookReqDTO));
    }
}
