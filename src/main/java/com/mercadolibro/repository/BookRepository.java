package com.mercadolibro.repository;

import com.mercadolibro.dto.AuthorBookCountDTO;
import com.mercadolibro.entity.Book;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Query(value = "SELECT JSON_EXTRACT(authors, '$[0].name') as authorName FROM book",
            nativeQuery = true)
    List<String> findAllAuthors(Pageable pageable);

    default Page<AuthorBookCountDTO> countBooksByAuthor(Pageable pageable) {
        List<String> allAuthors = findAllAuthors(Pageable.unpaged());

        Map<String, Long> authorCounts = allAuthors.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<AuthorBookCountDTO> dtos = authorCounts.entrySet().stream()
                .map(entry -> new AuthorBookCountDTO(entry.getKey().replace("\"", ""), entry.getValue()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());

        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

}
