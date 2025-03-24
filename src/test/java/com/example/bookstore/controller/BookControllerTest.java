package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;


    @Test
    void getBooks_withAllParameters_shouldReturnFilteredResults() throws Exception {
        // Given
        Book book = Book.builder()
            .id(1L)
            .title("Clean Code")
            .author("Robert Martin")
            .genre(Genre.FICTION)
            .yearOfPublication(2008)
            .isbn("123-456")
            .price(new BigDecimal("50.00"))
            .build();

        BookDTO bookDTO = BookDTO.builder()
            .id(1L)
            .title("Clean Code")
            .author("Robert Martin")
            .genre(Genre.FICTION)
            .yearOfPublication(2008)
            .isbn("123-456")
            .price(new BigDecimal("50.00"))
            .build();

        Page<Book> mockPage = new PageImpl<>(List.of(book));
        when(bookService.searchBooks(
            eq("Clean"),
            eq("Martin"),
            eq(Genre.FICTION),
            eq(2008),
            any(Pageable.class))
        ).thenReturn(mockPage);


        // When & Then
        mockMvc.perform(get("/books")
                .param("title", "Clean")
                .param("author", "Martin")
                .param("genre", "FICTION")
                .param("year", "2008")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title", is("Clean Code")))
            .andExpect(jsonPath("$.content[0].author", is("Robert Martin")))
            .andExpect(jsonPath("$.page.size", is(1)))
            .andExpect(jsonPath("$.page.number", is(0)))
            .andExpect(jsonPath("$.page.totalPages", is(1)));


        verify(bookService).searchBooks("Clean", "Martin", Genre.FICTION, 2008, PageRequest.of(0, 10)); // Corrected here as well
    }

    @Test
    void getBooks_withNoParameters_shouldUseDefaultPagination() throws Exception {
        // Given
        Page<Book> mockPage = new PageImpl<>(Collections.emptyList());
        when(bookService.searchBooks(null, null, null, null, PageRequest.of(0, 10)))
            .thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(0)))
            .andExpect(jsonPath("$.page.size", is(0)))
            .andExpect(jsonPath("$.page.number", is(0)))
            .andExpect(jsonPath("$.page.totalPages", is(1)));
    }

    @Test
    void getBooks_withPartialParameters_shouldUseProvidedFilters() throws Exception {
        // Given
        Book book = Book.builder()
            .id(2L)
            .title("Java Concurrency")
            .author("Brian Goetz")
            .genre(Genre.FICTION)
            .yearOfPublication(2016)
            .isbn("789-012")
            .price(new BigDecimal("60.00"))
            .build();

        Page<Book> mockPage = new PageImpl<>(List.of(book));
        when(bookService.searchBooks(
            eq("Java"),
            eq(null),
            eq(null),
            eq(null),
            any(Pageable.class))
        ).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/books")
                .param("title", "Java")
                .param("page", "2")
                .param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title", is("Java Concurrency")))
            .andExpect(jsonPath("$.content[0].author", is("Brian Goetz")))
            .andExpect(jsonPath("$.page.size", is(1)))
            .andExpect(jsonPath("$.page.number", is(0)))
            .andExpect(jsonPath("$.page.totalPages", is(1)));

        verify(bookService).searchBooks("Java", null, null, null, PageRequest.of(2, 20));
    }
}