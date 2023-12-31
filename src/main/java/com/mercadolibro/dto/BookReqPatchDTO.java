package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mercadolibro.dto.deserializer.DateDeserializer;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "Book Patch Request DTO", value = "BookReqPatch")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class BookReqPatchDTO {
    @Size(min = 1, max = 70)
    @ApiModelProperty(value = "Title of the book", example = "El inmortal")
    private String title;

    @Size(min = 1, max = 255)
    @Type(type = "json")
    @ApiModelProperty(value = "JSON String with information about the authors of the book", required = true,
            example = "[{'name': 'Jorge Luis Borges', 'email': 'jorgito556@gmail.com'}]")
    private List<AuthorDTO> authors;

    @Size(min = 1, max = 70)
    @ApiModelProperty(value = "Publisher of the book", example = "Editorial Planeta")
    private String publisher;

    @Past
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("published_date")
    @ApiModelProperty(value = "Published date of the book", example = "1991-06-14")
    private LocalDate publishedDate;

    @Size(min = 1, max = 255)
    @ApiModelProperty(value = "Description of the book", required = true, example =
            "Este nuevo texto, relata que, huyendo de unos sediciosos, Rufo se pierde " +
                    "en el desierto y se encuentra con un jinete moribundo que buscaba.")
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

    @ApiModelProperty(value = "Upload at least 1 and at most 5 files of the images.", example = "[\"images[]\"=\"file1.jpg\", \"images[]\"=\"file2.jpg\"]")
    private List<MultipartFile> images = new ArrayList<>();

    @JsonProperty("remove_images_ids")
    @ApiModelProperty(value = "Select at least 1 and at most 5 IDs of the images to delete.", example = "[\"id\": 1, \"id\": 2]")
    private List<Long> removeImagesIDs = new ArrayList<>();

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
    @ApiModelProperty(value = "JSON with the IDs of categories of the book", required = true,
            example = "[{\"id\": 1}]")
    private Set<BookCategoryReqDTO> categories;
}
