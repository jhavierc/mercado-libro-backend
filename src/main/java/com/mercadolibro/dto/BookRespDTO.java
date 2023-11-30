package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mercadolibro.dto.deserializer.DateDeserializer;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Book Response DTO", value = "BookResp")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class BookRespDTO {
    @ApiModelProperty(value = "ID of the book", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Title of the book", example = "El inmortal")
    private String title;

    @Type(type = "json")
    @ApiModelProperty(value = "Authors of the book",
            example = "[{'name': 'Jorge Luis Borges', 'email': 'jorgito556@gmail.com'}]")
    private List<AuthorDTO> authors;

    @ApiModelProperty(value = "Publisher of the book", example = "Editorial Planeta")
    private String publisher;

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("published_date")
    @ApiModelProperty(value = "Published date of the book", example = "1991-06-14")
    private LocalDate publishedDate;

    @ApiModelProperty(value = "Description of the book", example =
            "Este nuevo texto, relata que, huyendo de unos sediciosos, Rufo se pierde " +
                    "en el desierto y se encuentra con un jinete moribundo que buscaba.")
    private String description;

    @ApiModelProperty(value = "ISBN of the book", example = "978-2-0116-5274-4")
    private String isbn;

    @JsonProperty("page_count")
    @ApiModelProperty(value = "Page count of the book", example = "500")
    private Short pageCount;

    @JsonProperty("ratings_count")
    @ApiModelProperty(value = "Ratings count of the book", example = "4")
    private Short ratingsCount;

    @JsonProperty("image_links")
    @ApiModelProperty(value = "Image links of the book", example =
            "[\"https://link1.com/image.jpg\",\"https://link2.com/image.jpg\"]")
    private List<ImageDTO> imageLinks;

    @ApiModelProperty(value = "Language of the book", example = "ES")
    private String language;

    @ApiModelProperty(value = "Price of the book", example = "25.5")
    private BigDecimal price;

    @JsonProperty("currency_code")
    @ApiModelProperty(value = "Currency code of the book", example = "EUR")
    private String currencyCode;

    @ApiModelProperty(value = "Available stock of the book", example = "460")
    private Integer stock;

    @ApiModelProperty(value = "Categories of the book", example = "[{\"id\": 1}]")
    private Set<CategoryRespDTO> categories;

    @JsonProperty("created_at")
    @ApiModelProperty(value = "Date and time of creation", example = "2023-11-03T06:35:17")
    private LocalDateTime createdAt = LocalDateTime.now();
}