package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mercadolibro.dto.deserializer.DateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "Book Request DTO", value = "BookReq")
public class BookReqDTO {
    @Size(min = 1, max = 70)
    @NotBlank
    @ApiModelProperty(value = "Title of the book", required = true, example = "El inmortal")
    private String title;

    @Size(min = 1, max = 255)
    @NotBlank
    @ApiModelProperty(value = "JSON String with information about the authors of the book", required = true, example = "[{'name': 'Jorge Luis Borges', 'email': 'jorgito556@gmail.com'}]")
    private String authors;

    @Size(min = 1, max = 70)
    @NotBlank
    @ApiModelProperty(value = "Publisher of the book", required = true, example = "Editorial Planeta")
    private String publisher;

    @Past
    @NotNull
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("published_date")
    @ApiModelProperty(value = "Published date of the book", required = true, example = "1991-06-14")
    private LocalDate publishedDate;

    @Size(min = 1, max = 255)
    @NotBlank
    @ApiModelProperty(value = "Description of the book", required = true, example = "Este nuevo texto, relata que, huyendo de unos sediciosos, Rufo se pierde en el desierto y se encuentra con un jinete moribundo que buscaba.")
    private String description;

    @NotBlank
    @ISBN(type = ISBN.Type.ANY)
    @ApiModelProperty(value = "ISBN of the book", required = true, example = "978-2-0116-5274-4")
    private String isbn;

    @Range(min = 0, max = 10000)
    @NotNull
    @JsonProperty("page_count")
    @ApiModelProperty(value = "Page count of the book", required = true, example = "500")
    private Short pageCount;

    @NotNull
    @Range(min = 0, max = 5)
    @JsonProperty("ratings_count")
    @ApiModelProperty(value = "Ratings count of the book", required = true, example = "4")
    private Short ratingsCount;

    @Size(min = 1, max = 5)
    @NotNull
    @JsonIgnore
    @ApiModelProperty(value = "Select at least 1 and at most 5 images for the book. You can upload multiple images.")
    private List<MultipartFile> images;

    @NotBlank
    @ApiModelProperty(value = "Language of the book", required = true, example = "ES")
    private String language;

    @DecimalMin("1.0")
    @NotNull
    @ApiModelProperty(value = "Price of the book", required = true, example = "25.5")
    private BigDecimal price;

    @Size(min = 1, max = 10)
    @NotBlank
    @JsonProperty("currency_code")
    @ApiModelProperty(value = "Currency code of the book", required = true, example = "EUR")
    private String currencyCode;

    @Min(0)
    @NotNull
    @ApiModelProperty(value = "Available stock of the book", required = true, example = "460")
    private Integer stock;

    @Valid
    @Size(min = 1, max = 10)
    @NotNull
    @ApiModelProperty(value = "JSON with the IDs of categories of the book", required = true, example = "[{\"id\": 1}]")
    private Set<BookCategoryReqDTO> categories;
}
