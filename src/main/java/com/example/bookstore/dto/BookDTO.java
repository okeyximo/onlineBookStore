package com.example.bookstore.dto;

import com.example.bookstore.entity.Genre;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;


@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotEmpty(message = "Title is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Title must contain only letters and numbers")
    private String title;

    @NotNull(message = "Genre is required")
    private Genre genre;

    @NotEmpty(message = "ISBN is required")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dashes")
    private String isbn;

    @NotEmpty(message = "Author is required")
    private String author;

    @Min(value = 1000, message = "Year must be after 1000")
    @Max(value = 2100, message = "Year must be before 2100")
    private int yearOfPublication;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
}