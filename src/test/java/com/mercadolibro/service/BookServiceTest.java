package com.mercadolibro.service;

import com.mercadolibro.dto.BookReqDTO;
import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.BookCategoryReqDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Test
    @Order(1)
    public void saveTest() {
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
                .categories(new HashSet<>(Set.of(BookCategoryReqDTO.builder().id(1L).build())))
                .build();
        BookRespDTO saved = bookService.save(toSave);
        assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    public void findAllByCategory() {
        String category = "Ficcion";
        List<BookRespDTO> searched = bookService.findAllByCategory(category);
        assertFalse(searched.isEmpty());
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

}