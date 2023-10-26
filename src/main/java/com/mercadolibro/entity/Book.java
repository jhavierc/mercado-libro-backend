package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "book")
@RequiredArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String authors;

    @Column
    private String publisher;

    @JsonProperty("published_date")
    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column
    private String description;

    @Column(unique = true)
    private String isbn;

    @JsonProperty("page_count")
    @Column(name = "page_count")
    private Short pageCount;

    @JsonProperty("ratings_count")
    @Column(name = "ratings_count")
    private Short ratingsCount;

    @JsonProperty("image_links")
    @Column(name = "image_links")
    private ArrayList<String> imageLinks;

    @Column
    private String language;

    @Column
    private BigDecimal price;

    @JsonProperty("currency_code")
    @Column(name = "currency_code")
    private String currencyCode;

    @Column
    private Integer stock;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
}
