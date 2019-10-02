package com.company.BookService.dao;

import com.company.BookService.dto.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Book book2 = new Book();
        book2.setTitle("Book Title2");
        book2.setAuthor("Author Name2");

        bookRepo.save(book);
        bookRepo.save(book2);

        Book getBook = bookRepo.getOne(book.getId());
        assertEquals("Author Name", book.getAuthor());

    }

    @Test
    public void getAllBookTest(){

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        Book book2 = new Book();
        book2.setTitle("Book Title2");
        book2.setAuthor("Author Name2");

        bookRepo.save(book);
        bookRepo.save(book2);

        List<Book> books = bookRepo.findAll();
        assertEquals(2, books.size());

    }

    @Test
    public void deleteBookTest(){

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        Book book2 = new Book();
        book2.setTitle("Book Title2");
        book2.setAuthor("Author Name2");

        bookRepo.save(book);
        bookRepo.save(book2);

        bookRepo.deleteById(book.getId());
        List<Book> books = bookRepo.findAll();

        assertEquals(1, books.size());
    }

    @Test
    public void updateBookTest(){
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");
        bookRepo.save(book);

        book.setAuthor("Author Name2");
        bookRepo.save(book);

        Book getBook = bookRepo.getOne(book.getId());
        assertEquals("Author Name2", book.getAuthor());


    }

}