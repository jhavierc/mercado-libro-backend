package com.mercadolibro.service;

import com.mercadolibro.dto.*;
import com.mercadolibro.exceptions.BookAlreadyExistsException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceSaveFindTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        if (testInfo.getTags().contains("needsBook")) {
            saveBook();
        }
    }

    @Test
    @Order(1)
    public void saveTest() {
        BookRespDTO saved = saveBook();
        assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    @Tag("needsBook")
    public void findAllByCategory() {
        String category = "Ficcion";
        List<BookRespDTO> searched = bookService.findAllByCategory(category);
        assertFalse(searched.isEmpty());
        Optional<CategoryRespDTO> firstBookCategory = searched.get(0).getCategories().stream()
                .filter(cat -> cat.getName().equalsIgnoreCase(category)).findFirst();
        assertTrue(firstBookCategory.isPresent());
    }

    @Test
    @Order(3)
    @Tag("needsBook")
    public void findAll() {
        List<BookRespDTO> searched = bookService.findAll();
        assertFalse(searched.isEmpty());
    }

    @Test
    @Order(4)
    @Tag("needsBook")
    public void findByID() {
        BookRespDTO searched = bookService.findByID(1L);
        assertNotNull(searched);
        assertEquals("El Alquimista", searched.getTitle());
    }

    @Test
    @Order(5)
    @Tag("needsBook")
    public void testBookAlreadyExistsByISBN() {
        // Guardar un libro
        BookRespDTO savedBook = saveBook();

        // Intentar guardar el mismo libro nuevamente con el mismo ISBN
        BookReqDTO toSave = BookReqDTO.builder()
                .title("El Alquimista")
                .authors(new ArrayList<>(Arrays.asList("[{'name': 'Paulo Coelho', 'email': 'paulo96@hotmail.com'}]")))
                .publisher("Editorial Planeta")
                .publishedDate(LocalDate.of(1991, 06, 14))
                .description("El alquimista (O Alquimista, 1988) es una novela escrita por el escritor brasileño " +
                        "Paulo Coelho. Fue traducida a 88 lenguas y publicada en más de 170 países con más de 85 " +
                        "millones de copias en todo el mundo.")
                .isbn(savedBook.getIsbn())
                .pageCount((short) 500)
                .ratingsCount((short) 4)
                .imageLinks(new ArrayList<>(Arrays.asList("https://previews.123rf.com/images/will46/will462102/will46" +
                        "210200019/164816028-un-perrito-marr%C3%B3n-joven-y-muy-lindo-sentado-y-sonriendo-para-ser" +
                        "-usado-como-pegatina.jpg")))
                .language("ES")
                .price(BigDecimal.valueOf(25.5))
                .currencyCode("EUR")
                .stock(460)
                .categories(Set.of(BookCategoryReqDTO.builder().id(savedBook.getCategories().stream().findFirst().get().getId()).build()))
                .build();

        Exception e = assertThrows(BookAlreadyExistsException.class, () -> bookService.save(toSave));
        assertEquals("Book already exists", e.getMessage());
    }

    private String generateISBN() {
        String isbnFormat = "%d-%s-%s-%d";
        Random random = new Random();
        return String.format(isbnFormat, random.nextInt(10), addZero(random.nextInt(10000)),
                addZero(random.nextInt(10000)), random.nextInt(10));
    }

    private String addZero(int number) {
        if (number < 10) {
            return "000" + number;
        }

        if (number < 100) {
            return "00" + number;
        }

        if (number < 1000) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    private BookRespDTO saveBook() {
        CategoryReqDTO categoryToSave = CategoryReqDTO.builder()
                .name("Ficcion")
                .status("active")
                .description("Se denomina ficción a la simulación de la realidad que realizan las obras como " +
                        "literarias, cinematográficas, historietíscas, de animación o de otro tipo, cuando presentan " +
                        "un mundo imaginario al receptor.")
                .imageLink("https://previews.123rf.com/images/will46/will462102/will46210200019/164816028-un" +
                        "-perrito-marr%C3%B3n-joven-y-muy-lindo-sentado-y-sonriendo-para-ser-usado-como-pegatina.jpg")
                .build();
        Long categoryID = categoryService.save(categoryToSave).getId();

        BookReqDTO toSave = BookReqDTO.builder()
                .title("El Alquimista")
                .authors(new ArrayList<>(Arrays.asList("[{'name': 'Paulo Coelho', 'email': 'paulo96@hotmail.com'}]")))
                .publisher("Editorial Planeta")
                .publishedDate(LocalDate.of(1991, 06, 14))
                .description("El alquimista (O Alquimista, 1988) es una novela escrita por el escritor brasileño " +
                        "Paulo Coelho. Fue traducida a 88 lenguas y publicada en más de 170 países con más de 85 " +
                        "millones de copias en todo el mundo.")
                .isbn(generateISBN())
                .pageCount((short) 500)
                .ratingsCount((short) 4)
                .imageLinks(new ArrayList<>(Arrays.asList("https://previews.123rf.com/images/will46/will462102/will46" +
                        "210200019/164816028-un-perrito-marr%C3%B3n-joven-y-muy-lindo-sentado-y-sonriendo-para-ser" +
                        "-usado-como-pegatina.jpg")))
                .language("ES")
                .price(BigDecimal.valueOf(25.5))
                .currencyCode("EUR")
                .stock(460)
                .categories(Set.of(BookCategoryReqDTO.builder().id(categoryID).build()))
                .build();
        return bookService.save(toSave);
    }

}