package com.mercadolibro.service;

import com.mercadolibro.dto.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    private Long id = 1L;

    @Test
    @Order(1)
    public void saveTest() {
        CategoryReqDTO toSave = CategoryReqDTO.builder()
                .name("Ficcion")
                .status("active")
                .description("Se denomina ficción a la simulación de la realidad que realizan las obras como " +
                        "literarias, cinematográficas, historietíscas, de animación o de otro tipo, cuando presentan " +
                        "un mundo imaginario al receptor.")
                .imageLink("https://previews.123rf.com/images/will46/will462102/will46210200019/164816028-un" +
                        "-perrito-marr%C3%B3n-joven-y-muy-lindo-sentado-y-sonriendo-para-ser-usado-como-pegatina.jpg")
                .build();
        CategoryRespDTO saved = categoryService.save(toSave);
        assertNotNull(saved.getId());
        this.id = saved.getId();
    }

    @Test
    @Order(2)
    public void findAll() {
        List<CategoryRespDTO> searched = categoryService.findAll();
        assertFalse(searched.isEmpty());
    }

    @Test
    @Order(3)
    public void findByID() {
        CategoryRespDTO searched = categoryService.findByID(this.id);
        assertNotNull(searched);
        assertEquals("Ficcion", searched.getName());
    }
}
