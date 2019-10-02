package com.company.BookService.dao;

import com.company.BookService.dto.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepo;

    @Before
    public void setUp() throws Exception {
        bookRepo.deleteAll();
    }

    @Test
    public void saveBookTest(){

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        bookRepo.save(book);

        List<Book> books = bookRepo.findAll();
        assertEquals(1, books.size());
    }

    @Test
    public void getBookByIdTest(){

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        bookRepo.save(book);

        Book book2 = new Book();
        book2.setTitle("Book Title");
        book2.setAuthor("Author Name");

        bookRepo.save(book);

    }

    @Test
    public void getAllBookTest(){

    }

    @Test
    public void deleteBookTest(){

    }

    @Test
    public void updateBookTest(){

    }

}