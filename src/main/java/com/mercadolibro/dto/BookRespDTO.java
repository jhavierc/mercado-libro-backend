package com.mercadolibro.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookRespDTO {
    private Long id;
    private String title;
    private List<String> authors;
    private String publisher;
    private LocalDate publishedDate;
    private String description;
    private String isbn;
    private short pageCount;
    private short ratingsCount;
    private List<String> imageLinks;
    private String language;
    private BigDecimal price;
    private String currencyCode;
    private int stock;
    private Set<CategoryRespDTO> categories;
}
