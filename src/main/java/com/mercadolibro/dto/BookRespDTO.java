package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mercadolibro.dto.deserializer.DateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "Book Response DTO", value = "BookResp")
public class BookRespDTO {
    @ApiModelProperty(value = "ID of the book", required = true, example = "1")
    private Long id;

    @Size(min = 1, max = 70)
    @ApiModelProperty(value = "Title of the book", example = "El inmortal")
    private String title;

    @Size(min = 1, max = 255)
    @ApiModelProperty(value = "Authors of the book", example = "[{'name': 'Jorge Luis Borges', 'email': 'jorgito556@gmail.com'}]")
    private String authors;

    @Size(min = 1, max = 70)
    @ApiModelProperty(value = "Publisher of the book", example = "Editorial Planeta")
    private String publisher;

    @Past
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("published_date")
    @ApiModelProperty(value = "Published date of the book", example = "1991-06-14")
    private LocalDate publishedDate;

    @Size(min = 1, max = 255)
    @ApiModelProperty(value = "Description of the book", example = "Este nuevo texto, relata que, huyendo de unos sediciosos, Rufo se pierde en el desierto y se encuentra con un jinete moribundo que buscaba.")
    private String description;

    @ISBN(type = ISBN.Type.ANY)
    @ApiModelProperty(value = "ISBN of the book", example = "978-2-0116-5274-4")
    private String isbn;

    @Range(min = 0, max = 10000)
    @JsonProperty("page_count")
    @ApiModelProperty(value = "Page count of the book", example = "500")
    private Short pageCount;

    @Range(min = 0, max = 5)
    @JsonProperty("ratings_count")
    @ApiModelProperty(value = "Ratings count of the book", example = "4")
    private Short ratingsCount;

    @Size(min = 1, max = 5)
    @JsonProperty("image_links")
    @ApiModelProperty(value = "Image links of the book", example = "[\"https://link1.com/image.jpg\",\"https://link2.com/image.jpg\"]")
    private ArrayList<String> imageLinks;

    @ApiModelProperty(value = "Language of the book", example = "ES")
    private String language;

    @DecimalMin("1.0")
    @ApiModelProperty(value = "Price of the book", example = "25.5")
    private BigDecimal price;

    @Size(min = 1, max = 10)
    @JsonProperty("currency_code")
    @ApiModelProperty(value = "Currency code of the book", example = "EUR")
    private String currencyCode;

    @Min(0)
    @ApiModelProperty(value = "Available stock of the book", example = "460")
    private Integer stock;

    @Valid
    @Size(min = 1, max = 10)
    @ApiModelProperty(value = "Categories of the book", example = "[{\"id\": 1}]")
    private Set<CategoryRespDTO> categories;

    @JsonProperty("created_at")
    @ApiModelProperty(value = "Date and time of creation", example = "2023-11-03T06:35:17")
    private LocalDateTime createdAt = LocalDateTime.now();
}