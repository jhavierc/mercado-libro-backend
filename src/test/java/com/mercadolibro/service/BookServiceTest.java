package com.mercadolibro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.Category;
import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.exception.CategoryNotFoundException;
import com.mercadolibro.exception.NoBooksToShowException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.CategoryRepository;
import com.mercadolibro.service.impl.BookServiceImpl;
import com.mercadolibro.service.impl.CategoryServiceImpl;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static com.mercadolibro.service.impl.BookServiceImpl.BOOK_ISBN_ALREADY_EXISTS_ERROR_FORMAT;
import static com.mercadolibro.service.impl.BookServiceImpl.NOT_FOUND_ERROR_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @BeforeEach
    public void setUp() {
        objectMapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("should save book")
    public void testSaveBook() {
        // GIVEN
        BookCategoryReqDTO category = BookCategoryReqDTO.builder()
                .id(1L)
                .build();
        BookReqDTO book = BookReqDTO.builder()
                .isbn("0-7921-0519-2")
                .categories(Set.of(category))
                .build();

        // WHEN
        Book mockResponse = Book.builder()
                .id(1L)
                .isbn("0-7921-0519-2")
                .categories(Set.of(
                        Category.builder()
                                .id(1L)
                                .build()))
                .build();

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockResponse);

        // THEN
        BookRespDTO result = bookService.save(book);
        assertNotNull(result);
        assertNotNull(result.getCategories());
        assertEquals(result.getId(), 1L);
        verify(categoryRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).existsByIsbn(book.getIsbn());
    }

    @Test
    @DisplayName("should throw CategoryNotFoundException when tries to save a book with a non existing category")
    public void testDontSaveBookDueToCategory() {
        // GIVEN
        BookCategoryReqDTO category = BookCategoryReqDTO.builder()
                .id(1L)
                .build();
        BookReqDTO book = BookReqDTO.builder()
                .isbn("0-7921-0519-2")
                .categories(Set.of(category))
                .build();

        // WHEN
        when(categoryRepository.existsById(1L)).thenReturn(false);

        // THEN
        assertThrows(CategoryNotFoundException.class, () -> bookService.save(book));
    }

    @Test
    @DisplayName("should throw BookAlreadyExistsException when tries to save a book with an existing ISBN")
    public void testDontSaveBookDueToISBN() {
        // GIVEN
        BookCategoryReqDTO category = BookCategoryReqDTO.builder()
                .id(1L)
                .build();
        BookReqDTO book = BookReqDTO.builder()
                .isbn("0-7921-0519-2")
                .categories(Set.of(category))
                .build();

        // WHEN
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.existsByIsbn("0-7921-0519-2")).thenReturn(true);

        // THEN
        assertThrows(BookAlreadyExistsException.class, () -> bookService.save(book));
    }

    @Test
    @DisplayName("should find all books filtered by category")
    public void testFindAllByCategory() {
        // GIVEN
        String category = "rare category";

        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .categories(Set.of(Category.builder()
                                .name(category)
                                .build()))
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.unpaged(), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                category,
                null,
                false,
                false,
                false,
                false,
                false,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertTrue(books.get(0).getCategories().stream()
                .anyMatch(c -> Objects.equals(c.getName(), category)));
    }

    @Test
    @DisplayName("should find all books")
    public void testFindAll() {
        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .isbn("0-7921-0519-2")
                        .build(),
                Book.builder()
                        .isbn("0-8261-7049-8")
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.unpaged(), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("should throw NoBooksToShowException when tries to find all and there are no books")
    public void testFindNoBooks() {
        // WHEN
        Page<Book> mockResponse = new PageImpl<>(Collections.emptyList(), Pageable.unpaged(), 0);

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        assertThrows(NoBooksToShowException.class, () -> bookService.findAll(
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                (short) 1));
    }

    @Test
    @DisplayName("should find all books filtered by publisher")
    public void testFindAllByPublisher() {
        // GIVEN
        String publisher = "annoying publisher";

        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .publisher(publisher)
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.unpaged(), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                publisher,
                false,
                false,
                false,
                false,
                false,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals(publisher, books.get(0).getPublisher());
    }

    @Test
    @DisplayName("should find all books filtered by publisher and category")
    public void testFindAllByPublisherAndCategory() {
        // GIVEN
        String publisher = "happy publisher";
        String category = "sad category";

        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .publisher(publisher)
                        .categories(Set.of(Category.builder()
                                .name(category)
                                .build()))
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.unpaged(), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                category,
                publisher,
                false,
                false,
                false,
                false,
                false,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals(publisher, books.get(0).getPublisher());
        assertTrue(books.get(0).getCategories().stream()
                .anyMatch(c -> Objects.equals(c.getName(), category)));
    }

    @Test
    @DisplayName("should find all books sorted ascendant")
    public void testFindAllSortedAscendant() {
        // GIVEN
        boolean ascendant = true;

        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .title("amusing title")
                        .build(),
                Book.builder()
                        .title("sad title")
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(
                content,
                PageRequest.of(0, 9, Sort.by("title").ascending()),
                content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                eq(PageRequest.of(0, 9, Sort.by("title").ascending()))))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                null,
                false,
                false,
                false,
                ascendant,
                false,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals("amusing title", books.get(0).getTitle());
        assertEquals("sad title", books.get(1).getTitle());
    }

    @Test
    @DisplayName("should find all books sorted descendant")
    public void testFindAllSortedDescendant() {
        // GIVEN
        boolean descendant = true;

        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .title("sad title")
                        .build(),
                Book.builder()
                        .title("amusing title")
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(
                content,
                PageRequest.of(0, 9, Sort.by("title").ascending()),
                content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                eq(PageRequest.of(0, 9, Sort.by("title").descending()))))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                null,
                false,
                false,
                false,
                false,
                descendant,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals("amusing title", books.get(1).getTitle());
        assertEquals("sad title", books.get(0).getTitle());
    }

    @Test
    @DisplayName("should find all books filtered by releases")
    public void testFindAllByReleases() {
        // GIVEN
        boolean releases = true;

        // WHEN
        List<Book> content = List.of(
                Book.builder()
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.unpaged(), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                null,
                releases,
                false,
                false,
                false,
                false,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals(LocalDateTime.now().getMonthValue(), books.get(0).getCreatedAt().getMonthValue());
    }

    @Test
    @DisplayName("should update an existing book")
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
    @DisplayName("should throw BookNotFoundException when tries to update a non existing book")
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
    @DisplayName("should throw BookAlreadyExistsException when tries to update an existing book")
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
    @DisplayName("should patch an existing book")
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
    @DisplayName("should throw BookNotFoundException when tries to update a non existing book")
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
    @DisplayName("should throw BookAlreadyExistsException when tries to update an existing book")
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
    @DisplayName("should delete an existing book")
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
    @DisplayName("should throw BookNotFoundException when tries to delete a non existing book")
    void testDeleteNonExistingBook() {
        // Arrange
        Long bookId = 1L;

        doReturn(false).when(bookRepository).existsById(bookId);

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
    }
}
