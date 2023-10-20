package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookReqDTO {
    @Size(min = 1, max = 70)
    @NotBlank
    private String title;

    @Size(min = 1, max = 10)
    @NotNull
    private List<String> authors;

    @Size(min = 1, max = 70)
    @NotBlank
    private String publisher;

    @Past
    @NotNull
    @JsonProperty("published_date")
    private LocalDate publishedDate;

    @Size(min = 1, max = 1000)
    @NotBlank
    private String description;

    @NotBlank
    //@ISBN
    private String isbn;

    @Range(min = 0, max = 10000)
    @NotNull
    @JsonProperty("page_count")
    private Short pageCount;

    @NotNull
    @Range(min = 0, max = 5)
    @JsonProperty("ratings_count")
    private Short ratingsCount;

    @Size(min = 1, max = 5)
    @NotNull
    @JsonProperty("image_links")
    private List<String> imageLinks;

    @NotBlank
    private String language;

    @DecimalMin("1.0")
    @NotNull
    private BigDecimal price;

    @Size(min = 1, max = 10)
    @NotBlank
    @JsonProperty("currency_code")
    private String currencyCode;

    @Min(0)
    @NotNull
    private Integer stock;

    @Size(min = 1, max = 10)
    private Set<CategoryReqDTO> categories;
}
