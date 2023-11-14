package com.mercadolibro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.Category;
import com.mercadolibro.exception.BookAlreadyExistsException;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.exception.CategoryNotFoundException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
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
    private S3Service s3Service;
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
        String firstImageLink = "https://my-bucket-name.s3.amazonaws.com/images/example-file-1.jpg";
        String secondImageLink = "https://my-bucket-name.s3.amazonaws.com/images/example-file-2.jpg";

        ArrayList<String> imageLinks = new ArrayList<>();
        imageLinks.add(firstImageLink);
        imageLinks.add(secondImageLink);

        MockMultipartFile file1 = new MockMultipartFile("example-file-1", "example-file-1.jpg", "image/png", new byte[0]);
        MockMultipartFile file2 = new MockMultipartFile("example-file-2", "example-file-2.jpg", "image/png", new byte[0]);
        List<MultipartFile> files = Arrays.asList(file1, file2);

        S3ObjectDTO s3ObjectDTO1 = new S3ObjectDTO("images/example-file-1.jpg", firstImageLink, "my-bucket-name");
        S3ObjectDTO s3ObjectDTO2 = new S3ObjectDTO("images/example-file-2.jpg", secondImageLink, "my-bucket-name");
        List<S3ObjectDTO> s3ObjectDTOS = Arrays.asList(s3ObjectDTO1, s3ObjectDTO2);

        BookCategoryReqDTO category = BookCategoryReqDTO.builder()
                .id(1L)
                .build();
        BookReqDTO book = BookReqDTO.builder()
                .title("a title")
                .isbn("0-7921-0519-2")
                .images(files)
                .categories(Set.of(category))
                .build();

        Book mockResponse = Book.builder()
                .id(1L)
                .title("a title")
                .isbn("0-7921-0519-2")
                .imageLinks(imageLinks)
                .categories(Set.of(
                        Category.builder()
                                .id(1L)
                                .build()))
                .build();

        // WHEN
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockResponse);
        when(s3Service.uploadFiles(anyList())).thenReturn(s3ObjectDTOS);

        // THEN
        BookRespDTO result = bookService.save(book);

        assertNotNull(result);
        assertEquals(result.getId(), 1L);
        assertEquals("0-7921-0519-2", result.getIsbn());
        assertNotNull(result.getCategories());
        assertEquals("a title", result.getTitle());
        assertEquals(imageLinks, result.getImageLinks());

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
                null,
                null,
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
                null,
                null,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertFalse(books.isEmpty());
        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("should return an empty page when tries to find all and there are no books")
    public void testFindNoBooks() {
        // WHEN
        Page<Book> mockResponse = new PageImpl<>(Collections.emptyList(), Pageable.ofSize(9), 0);

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                "random",
                null,
                false,
                null,
                null,
                (short) 1);

        List<BookRespDTO> books = result.getContent();

        assertTrue(books.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
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
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.ofSize(9), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                publisher,
                false,
                null,
                null,
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
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.ofSize(9), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                category,
                publisher,
                false,
                null,
                null,
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
        String sort = "asc";

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
                null,
                sort,
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
        String sort = "desc";

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
                null,
                sort,
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
        Page<Book> mockResponse = new PageImpl<>(content, Pageable.ofSize(9), content.size());

        when(bookRepository.findAll(
                ArgumentMatchers.<Specification<Book>>any(),
                ArgumentMatchers.<Pageable>any()))
                .thenReturn(mockResponse);

        // THEN
        PageDTO<BookRespDTO> result = bookService.findAll(
                null,
                null,
                releases,
                null,
                null,
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
        BookReqPatchDTO input = new BookReqPatchDTO();

        String imageURL1 = "https://my-bucket-name.s3.amazonaws.com/images/example-file-1.jpg";
        String imageURL2 = "https://my-bucket-name.s3.amazonaws.com/images/example-file-2.jpg";
        List<String> imagesToReplace = new ArrayList<>();

        imagesToReplace.add(imageURL1);
        imagesToReplace.add(imageURL2);

        input.setImagesToReplaceURLs(imagesToReplace);
        String imageLinks = imageURL1 + "," + imageURL2;

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle(title);

        Book mockRepositoryResponse = new Book();
        mockRepositoryResponse.setId(bookId);
        mockRepositoryResponse.setTitle(title);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(mockRepositoryResponse);
        when(bookRepository.findImageLinksById(bookId)).thenReturn(imageLinks);

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
        BookReqPatchDTO input = new BookReqPatchDTO();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act and Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class,
                () -> bookService.patch(bookId, input));
        assertEquals(String.format(NOT_FOUND_ERROR_FORMAT, "book", bookId), exception.getMessage());
    }

    @Test
    @DisplayName("should throw BookAlreadyExistsException when tries to update an existing book")
    void testPatchExistingBookISBN() {
        // Arrange
        Long bookId = 1L;
        String actualBookIsbn = "978-0-261-10238-4";
        String alreadyExistingBookIsbn = "978-0-261-10238-4";

        BookReqPatchDTO input = new BookReqPatchDTO();
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
        String images = "https://my-bucket-name.s3.amazonaws.com/images/example-file-1.jpg," +
                "https://my-bucket-name.s3.amazonaws.com/images/example-file-2.jpg";

        doReturn(true).when(bookRepository).existsById(bookId);
        when(bookRepository.findImageLinksById(bookId)).thenReturn(images);
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
    @Test
    public void testFindByTitleOrDescriptionContaining() {
        // GIVEN
        String keyword = "test";
        short page = 0;
        Pageable pageable = PageRequest.of(page, 9);

        List<Book> mockBooks = Arrays.asList(new Book(), new Book());
        Page<Book> mockPage = new PageImpl<>(mockBooks, pageable, mockBooks.size());
        when(bookRepository.findByTitleOrDescriptionContaining(eq(keyword), any(Pageable.class))).thenReturn(mockPage);

        // WHEN
        PageDTO<BookRespDTO> result = bookService.findByTitleOrDescriptionContaining(keyword, page);

        // THEN
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(mockPage.getTotalPages(), result.getTotalPages());
        assertEquals(mockPage.getTotalElements(), result.getTotalElements());
    }
}
