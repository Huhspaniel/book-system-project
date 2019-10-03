package com.company.BookService.service;

import com.company.BookService.dao.BookRepository;
import com.company.BookService.dto.Book;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.viewmodel.BookViewModel;
import com.company.BookService.viewmodel.NoteViewModel;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.*;

@SpringBootTest
public class BookServiceLayerTest {

    private BookServiceLayer bookServiceLayer;
    private BookRepository bookRepo;
    private RabbitTemplate rabbitTemplate;
    private NoteFeignClient feignClient;


    @Before
    public void setUp() throws Exception {

        setUpBookServiceMock();

        bookServiceLayer = new BookServiceLayer(rabbitTemplate, bookRepo, feignClient);

    }

    @Test
    public void create() {

        BookViewModel book = new BookViewModel();
        book.setTitle("Book Title");
        book.setAuthor("Author Name");
        book = bookServiceLayer.create(book);

        NoteViewModel note = new NoteViewModel();
        note.setId(1);
        note.setNote("jkfdls");

        BookViewModel fromService = bookServiceLayer.findById(book.getId());

        assertEquals(book, fromService);

    }

    @Test
    public void findById() {



    }

    @Test
    public void findAll() {



    }

    @Test
    public void update() {



    }

    @Test
    public void deleteById() {



    }


    private void setUpBookServiceMock(){

        bookRepo = mock(BookRepository.class);

        Book book = new Book();
        book.setId(1);
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        Book book2 = new Book();
        book2.setTitle("Book Title");
        book2.setAuthor("Author Name");

        List<Book> bookList = new ArrayList<>();
        bookList.add(book);

        doReturn(book).when(bookRepo).save(book2);
        doReturn(book).when(bookRepo).getOne(1);
        doReturn(bookList).when(bookRepo).findAll();

    }

}