package com.company.BookService.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="book")
public class Book implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name="book_id", nullable = false, unique = true)
    private Integer bookId;
    @Column(name="title", nullable=false, length=50)
    private String title;
    @Column(name="author", nullable=false, length=50)
    private String author;

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(Integer bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    public Integer getId() {
        return bookId;
    }

    public void setId(Integer id) {
        this.bookId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author);
    }

    @Override
    public Book clone() {
        try {
            return (Book) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
