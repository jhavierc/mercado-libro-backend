package com.mercadolibro.service.specification;

import com.mercadolibro.entity.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Calendar;

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

    /**
     * Builds a condition that validates that a book has been published in the current month.
     *
     * @return a Specification object that is used to construct a condition that filters the results of a query.
     */
    public static Specification<Book> releasesSpec() {
        return (root, query, criteriaBuilder) -> {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;

            return criteriaBuilder.and(
                    criteriaBuilder.equal(
                            criteriaBuilder.function("year", Integer.class, root.get("createdAt")),
                            currentYear
                    ),
                    criteriaBuilder.equal(
                            criteriaBuilder.function("month", Integer.class, root.get("createdAt")),
                            currentMonth
                    )
            );
        };
    }

    /**
     * Builds a specification that filters based on the keyword by title, author, or publisher.
     *
     * @param keyword The keyword by which it is filtering.
     * @return a Specification object used to build a condition that filters the results of a query.
     */
    public static Specification<Book> titleOrAuthorOrPublisherContainingSpec(String keyword) {
        String[] keywords = keyword.toLowerCase().split("\\s+");

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.isFalse(criteriaBuilder.literal(false));

            for (String kw : keywords) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + kw + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("authors")), "%" + kw + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher")), "%" + kw + "%")
                        )
                );
            }
            return predicate;
        };
    }
}
