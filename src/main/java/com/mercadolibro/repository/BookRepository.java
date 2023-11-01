package com.mercadolibro.repository;

import com.mercadolibro.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("FROM Book b JOIN b.categories c WHERE c.name = :category")
    List<Book> findAllByCategory(@Param("category") String category);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
