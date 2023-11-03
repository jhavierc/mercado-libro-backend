package com.mercadolibro.service.specification;

import com.mercadolibro.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class BookSpecification {
    /**
     * Builds a condition that validates that a book has the same category as the one provided.
     *
     * @param category The category by which it is compared.
     * @return a Specification object that is used to construct a condition that filters the results of a query.
     */
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

    /**
     * Builds a condition that validates that a book has the same publisher as the one provided.
     *
     * @param publisher The publisher by which it is compared.
     * @return a Specification object that is used to construct a condition that filters the results of a query.
     */
    public static Specification<Book> publisherSpec(String publisher) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("publisher"), publisher);
    }
}
