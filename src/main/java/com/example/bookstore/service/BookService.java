package com.example.bookstore.service;


import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> searchBooks(String title, String author, Genre genre, Integer year, Pageable pageable) {
        Specification<Book> spec = BookSpecification.searchBooks(title, author, genre, year);
        return bookRepository.findAll(spec, pageable);
    }
}