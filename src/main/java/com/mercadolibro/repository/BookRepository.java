package com.mercadolibro.repository;

import com.mercadolibro.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("FROM Book b JOIN b.categories c WHERE c.name = :category")
    List<Book> findAllByCategory(@Param("category") String category);

    @Query("SELECT b.imageLinks FROM Book b WHERE b.id = :id")
    String findImageLinksById(@Param("id") Long id);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
