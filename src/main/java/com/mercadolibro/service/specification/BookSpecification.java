package com.mercadolibro.service.specification;

import com.mercadolibro.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class BookSpecification {
    public static Specification<Book> categorySpec(String category) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Book> subqueryRoot = subquery.from(Book.class);
            subquery.select(criteriaBuilder.count(subqueryRoot));
            subquery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(subqueryRoot, root),
                            criteriaBuilder.equal(
                                    subqueryRoot.join("categories").get("name"),
                                    category
                            )
                    )
            );

            return criteriaBuilder.greaterThan(subquery, 0L);
        };
    }

    public static Specification<Book> publisherSpec(String publisher) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("publisher"), publisher);
        };
    }
}
