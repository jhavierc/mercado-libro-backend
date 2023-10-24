package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookRespDTO {
    private Long id;
    private String title;
    private List<String> authors;
    private String publisher;
    @JsonProperty("published_date")
    private LocalDate publishedDate;
    private String description;
    private String isbn;
    @JsonProperty("page_count")
    private Short pageCount;
    @JsonProperty("ratings_count")
    private Short ratingsCount;
    @JsonProperty("image_links")
    private ArrayList<String> imageLinks;
    private String language;
    private BigDecimal price;
    @JsonProperty("currency_code")
    private String currencyCode;
    private Integer stock;
    private Set<CategoryRespDTO> categories;
}