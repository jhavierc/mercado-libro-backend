package com.mercadolibro.repository;

import com.mercadolibro.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    //@Query("SELECT b.imageLinks FROM Book b WHERE b.id = :id")
    //String findImageLinksById(@Param("id") Long id);
    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
