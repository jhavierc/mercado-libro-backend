package com.mercadolibro.repository;

import com.mercadolibro.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.name = :category")
    Page<Book> findAllByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Book b JOIN b.categories c WHERE c.name = :category")
    long countByCategory(@Param("category") String category);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
