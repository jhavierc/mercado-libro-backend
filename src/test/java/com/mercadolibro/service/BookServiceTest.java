package com.mercadolibro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.Category;
import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.CategoryRepository;
import com.mercadolibro.service.impl.BookServiceImpl;
import com.mercadolibro.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT;
import static com.mercadolibro.service.impl.BookServiceImpl.NOT_FOUND_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void testSaveBook() {
        Long bookId = 1L;

        Book mockRepositoryResponse = new Book();
        mockRepositoryResponse.setId(bookId);
        mockRepositoryResponse.setTitle("a title");

        BookReqDTO input = new BookReqDTO();
        input.setTitle("a title");

        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockRepositoryResponse);

        BookRespDTO res = bookService.save(input);
        assertNotNull(res);
        assertEquals(input.getTitle(), res.getTitle());
        assertEquals(bookId, res.getId());
    }


    @Test
    public void testFindAllByCategory() {
        Long catID = 1L;
        String catName = "rare category";

        Category cat = new Category();
        cat.setId(catID);
        cat.setName(catName);

        Book a = new Book();
        a.setTitle("great title");
        a.setCategories(Set.of(cat));

        Book b = new Book();
        b.setTitle("boring title");
        b.setCategories(Set.of(cat));

        List<Book> books = List.of(a, b);

        Page<Book> mockRepositoryResponse = new PageImpl<>(books, Pageable.unpaged(), books.size());

        when(bookRepository.findAllByCategory(Mockito.any(String.class),
                Mockito.any(Pageable.class))).thenReturn(mockRepositoryResponse);

        List<BookRespDTO> res = bookService.findAllByCategory(catName, (short) 1);
        assertFalse(res.isEmpty());

        Optional<CategoryRespDTO> resCategory = res.get(0).getCategories().stream()
                .filter(category -> Objects.equals(category.getId(), catID)).findFirst();

        assertFalse(resCategory.isEmpty());
        assertEquals(resCategory.get().getId(), catID);
    }

    @Test
    public void testFindAll() {
        Book a = new Book();
        String aTitle = "great title";
        a.setTitle(aTitle);

        Book b = new Book();
        String bTitle = "boring title";
        b.setTitle(bTitle);

        List<Book> mockRepositoryResponse = List.of(a, b);

        when(bookRepository.findAll()).thenReturn(mockRepositoryResponse);

        List<BookRespDTO> res = bookService.findAll((short) 1);
        assertFalse(res.isEmpty());
        assertEquals(res.get(0).getTitle(), aTitle);
        assertEquals(res.get(1).getTitle(), bTitle);
    }

    @Test
    void testUpdateExistingBook() {
        // Arrange
        Long bookId = 1L;
        BookReqDTO input = new BookReqDTO();
        input.setLanguage("english");

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
        assertEquals(String.format(NOT_FOUND_ERROR_FORMAT, "book", bookId), exception.getMessage());
    }

    @Test
    void testUpdateExistingBookISBN() {
        // Arrange
        Long bookId = 1L;
        String bookIsbn = "978-0-261-10238-4";

        BookReqDTO input = new BookReqDTO();
        input.setIsbn(bookIsbn);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.existsByIsbnAndIdNot(bookIsbn, bookId)).thenReturn(true);

        // Act and Assert
        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class,
                () -> bookService.update(bookId, input));
        assertEquals(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, bookIsbn), exception.getMessage());
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

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookService.patch(bookId, input));
        assertEquals(String.format(NOT_FOUND_ERROR_FORMAT, "book", bookId), exception.getMessage());
        bookService = new BookServiceImpl(bookRepository, categoryRepository, new ObjectMapper(), new ModelMapper());
    }

    @Test
    void testPatchExistingBookISBN() {
        // Arrange
        Long bookId = 1L;
        String actualBookIsbn = "978-0-261-10238-4";
        String alreadyExistingBookIsbn = "978-0-261-10238-4";

        BookRespDTO input = new BookRespDTO();
        input.setIsbn(alreadyExistingBookIsbn);

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setIsbn(actualBookIsbn);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbnAndIdNot(alreadyExistingBookIsbn, bookId)).thenReturn(true);

        // Act and Assert
        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class,
                () -> bookService.patch(bookId, input));
        assertEquals(String.format(BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT, alreadyExistingBookIsbn), exception.getMessage());
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
