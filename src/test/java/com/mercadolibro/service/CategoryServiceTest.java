package com.mercadolibro.service;

import com.mercadolibro.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        if (testInfo.getTags().contains("needsCategory")) {
            saveCategory();
        }
    }

    @Test
    @Order(1)
    public void saveTest() {
        CategoryRespDTO saved = saveCategory();
        assertNotNull(saved.getId());
    }

    @Test
    @Order(2)
    @Tag("needsCategory")
    public void findAll() {
        List<CategoryRespDTO> searched = categoryService.findAll();
        assertFalse(searched.isEmpty());
    }

    @Test
    @Order(3)
    @Tag("needsCategory")
    public void findByID() {
        CategoryRespDTO searched = categoryService.findByID(1L);
        assertNotNull(searched);
        assertEquals("Ficcion", searched.getName());
    }

    private CategoryRespDTO saveCategory() {
        CategoryReqDTO toSave = CategoryReqDTO.builder()
                .name("Ficcion")
                .status("active")
                .description("Se denomina ficción a la simulación de la realidad que realizan las obras como " +
                        "literarias, cinematográficas, historietíscas, de animación o de otro tipo, cuando presentan " +
                        "un mundo imaginario al receptor.")
                .imageLink("https://previews.123rf.com/images/will46/will462102/will46210200019/164816028-un" +
                        "-perrito-marr%C3%B3n-joven-y-muy-lindo-sentado-y-sonriendo-para-ser-usado-como-pegatina.jpg")
                .build();
        return categoryService.save(toSave);
    }
}
