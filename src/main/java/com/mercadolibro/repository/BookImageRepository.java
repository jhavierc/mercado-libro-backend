package com.mercadolibro.repository;

import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long>, JpaSpecificationExecutor<BookImage> {

    @Query("SELECT b FROM BookImage b WHERE b.book.id = :bookID")
    public Optional<List<BookImage>> getByBookId(@Param("bookID") Long bookID);

}
