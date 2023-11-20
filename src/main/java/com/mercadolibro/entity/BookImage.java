package com.mercadolibro.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "book_images")
@Cacheable(false)
@Data
@NoArgsConstructor
@Getter
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @Column
    private String name;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;
}
