package com.mercadolibro.repository;

import com.mercadolibro.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("FROM Book b JOIN b.categories c WHERE c.name = :category")
    List<Book> findAllByCategory(@Param("category") String category);

    boolean existsByIsbn(String isbn);
}
