package com.mercadolibro.repository;

import com.mercadolibro.entity.Book;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    @NonNull
    @EntityGraph(attributePaths = {"categories", "images", "authors"})
    Optional<Book> findById(@Param("id") @NonNull Long id);

    @NonNull
    @EntityGraph(attributePaths = {"categories", "images", "authors"})
    Page<Book> findAll(Specification<Book> spec, @NonNull Pageable pageable);
}
