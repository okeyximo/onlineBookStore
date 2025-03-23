package com.example.bookstore.config;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Genre;
import com.example.bookstore.mapper.EntityMapper;
import com.example.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;

import com.example.bookstore.dto.BookDTO; // Adjust package as needed
import com.example.bookstore.entity.Book;   // Adjust package as needed
import com.example.bookstore.mapper.EntityMapper; // Adjust package as needed
import com.example.bookstore.repository.BookRepository; // Adjust package as needed
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final EntityMapper entityMapper;

    @Value("classpath:books.csv")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            try {
                List<Book> books = parseCsvFile();
                bookRepository.saveAll(books);
                log.info("Loaded {} books into database", books.size());
            } catch (Exception e) {
                log.error("Failed to load books from CSV: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    private List<Book> parseCsvFile() throws Exception {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            List<BookDTO> bookDTOs = new CsvToBeanBuilder<BookDTO>(reader)
                .withType(BookDTO.class)
                .build()
                .parse();
            return bookDTOs.stream()
                .map(entityMapper::toBook)
                .toList();
        }
    }
}