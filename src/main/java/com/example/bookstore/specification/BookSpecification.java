package com.example.bookstore.specification;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> searchBooks(String title, String author, Genre genre, Integer year) {
        return (Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (author != null && !author.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("author"), "%" + author + "%"));
            }

            if (genre != null) {
                predicates.add(criteriaBuilder.equal(root.get("genre"), genre));
            }

            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("yearOfPublication"), year));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}