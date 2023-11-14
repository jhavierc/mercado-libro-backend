package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String status;

    @Column
    private String description;

    @Column(name = "image_link")
    @JsonProperty("image_link")
    private String imageLink;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<Book> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(status, category.status)
                && Objects.equals(description, category.description)
                && Objects.equals(imageLink, category.imageLink)
                && Objects.equals(books, category.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, imageLink, books);
    }
}
