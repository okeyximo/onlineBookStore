package com.example.bookstore.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void searchBooks_WithParameters_ShouldReturnPage() {
        // Given
        String title = "Effective Java";
        String author = "Joshua Bloch";
        Genre genre = Genre.FICTION;
        Integer year = 2001;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> expectedPage = new PageImpl<>(Collections.singletonList(new Book()));

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // When
        Page<Book> result = bookService.searchBooks(title, author, genre, year, pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);

        // Verify repository interaction
        var specCaptor = org.mockito.ArgumentCaptor.forClass(Specification.class);
        var pageableCaptor = org.mockito.ArgumentCaptor.forClass(Pageable.class);

        verify(bookRepository, times(1)).findAll(specCaptor.capture(), pageableCaptor.capture());

        assertThat(pageableCaptor.getValue()).isEqualTo(pageable);
        assertThat(specCaptor.getValue()).isNotNull();
    }
}