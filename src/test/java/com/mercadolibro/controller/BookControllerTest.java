package com.mercadolibro.controller;

import com.mercadolibro.controllers.BookController;
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
        Long bookId = 1L;
        BookReqDTO bookReqDTO = new BookReqDTO();
        BookRespDTO bookRespDTO = new BookRespDTO();

        doReturn(bookRespDTO).when(bookService).update(bookId, bookReqDTO);

        ResponseEntity<Object> response = bookController.update(bookId, bookReqDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookRespDTO, response.getBody());

        Mockito.verify(bookService, Mockito.times(1)).update(bookId, bookReqDTO);
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
        Long bookId = 1L;
        BookReqDTO bookReqDTO = new BookReqDTO();
        BookRespDTO bookRespDTO = new BookRespDTO();

        doReturn(bookRespDTO).when(bookService).patch(bookId, bookReqDTO);

        ResponseEntity<Object> response = bookController.patch(bookId, bookReqDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookRespDTO, response.getBody());

        Mockito.verify(bookService, Mockito.times(1)).patch(bookId, bookReqDTO);
    }

    @Test
    public void testPatchNonExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO bookReqDTO = new BookReqDTO();
        String expectedErrorMessage = BOOK_NOT_FOUND;

        when(bookService.patch(bookId, bookReqDTO)).thenThrow(new ResourceNotFoundException(expectedErrorMessage));

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookController.patch(bookId, bookReqDTO));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}

