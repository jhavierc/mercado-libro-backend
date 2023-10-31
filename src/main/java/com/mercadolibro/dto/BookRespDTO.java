package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mercadolibro.dto.deserializer.DateDeserializer;
import lombok.*;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookRespDTO {
    private Long id;

    @Size(min = 1, max = 70)
    private String title;

    @Size(min = 1, max = 255)
    private String authors;

    @Size(min = 1, max = 70)
    private String publisher;

    @Past
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("published_date")
    private LocalDate publishedDate;

    @Size(min = 1, max = 255)
    private String description;

    @ISBN(type = ISBN.Type.ANY)
    private String isbn;

    @Range(min = 0, max = 10000)
    @JsonProperty("page_count")
    private Short pageCount;

    @Range(min = 0, max = 5)
    @JsonProperty("ratings_count")
    private Short ratingsCount;

    @Size(min = 1, max = 5)
    @JsonProperty("image_links")
    private ArrayList<String> imageLinks;

    private String language;

    @DecimalMin("1.0")
    private BigDecimal price;

    @Size(min = 1, max = 10)
    @JsonProperty("currency_code")
    private String currencyCode;

    @Min(0)
    private Integer stock;

    @Valid
    @Size(min = 1, max = 10)
    private Set<CategoryRespDTO> categories;
}