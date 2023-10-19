package com.mercadolibro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookReqDTO {
    @NotBlank
    @Size(min = 1, max = 70)
    private String title;

    @NotBlank
    @Size(min = 1, max = 10)
    private List<String> authors;

    @NotBlank
    @Size(min = 1, max = 70)
    private String publisher;

    @NotBlank
    @Past
    private LocalDate publishedDate;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String description;

    @NotBlank
    private String isbn;

    @NotBlank
    @Range(min = 0, max = 10000)
    private short pageCount;

    @NotBlank
    private short ratingsCount;

    @NotBlank
    @Size(min = 1, max = 5)
    private List<String> imageLinks;

    @NotBlank
    private String language;

    @NotBlank
    @DecimalMin("1.0")
    private BigDecimal price;

    @NotBlank
    @Size(min = 1, max = 10)
    private String currencyCode;

    @NotBlank
    @Min(0)
    private int stock;

    @NotBlank
    @Size(min = 1, max = 10)
    private Set<CategoryBookReqDTO> categories;
}
