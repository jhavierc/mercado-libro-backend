package com.mercadolibro.controller;

import com.mercadolibro.dto.BookReqPatchDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT;
import static com.mercadolibro.service.impl.BookServiceImpl.NOT_FOUND_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;

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
        BookReqPatchDTO input = new BookReqPatchDTO();
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
        BookReqPatchDTO input = new BookReqPatchDTO();
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
        BookReqPatchDTO input = new BookReqPatchDTO();
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

    @Test
    public void testSearchBooks() {
        // GIVEN
        String keyword = "Harry Potter";
        short page = 0;

        PageDTO<BookRespDTO> mockPageDTO = new PageDTO<>(List.of(new BookRespDTO()), 1, 1L, 0, 1);
        when(bookService.findAll(eq(keyword),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Boolean.class),
                ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(String.class),
                eq(page))).thenReturn(mockPageDTO);

        // WHEN
        ResponseEntity<PageDTO<BookRespDTO>> response = bookController.findAll(keyword, null, null,
                false, null, null, page);

        // THEN
        assertEquals(200, response.getStatusCodeValue()); // Verificar que la respuesta sea OK
        assertEquals(mockPageDTO, response.getBody()); // Verificar que el cuerpo de la respuesta coincida con el PageDTO simulado
    }
}
