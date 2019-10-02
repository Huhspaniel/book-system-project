package com.company.BookService.service;

import com.company.BookService.dao.BookRepository;
import com.company.BookService.dto.Book;
import com.company.BookService.dto.Note;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.viewmodel.BookViewModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookServiceLayer {
    private static final String EXCHANGE = "note-exchange";
    private static final String ROUTING_KEY = "note.bookService";
    private RabbitTemplate rabbit;
    private BookRepository bookRepo;
    private NoteFeignClient noteClient;

    @Autowired
    public BookServiceLayer(RabbitTemplate rabbit, BookRepository bookRepo, NoteFeignClient noteClient) {
        this.rabbit = rabbit;
        this.bookRepo = bookRepo;
        this.noteClient = noteClient;
    }
    @Transactional
    public BookViewModel create(BookViewModel bvm) {
        Book book = new Book();
        book.setId(bvm.getId());
        book.setTitle(bvm.getTitle());
        book.setAuthor(bvm.getAuthor());
        book = bookRepo.save(book);

        bvm.setId(book.getId());

        return bvm;
    }

    public BookViewModel findById(Integer id) {
            Book book = bookRepo.getOne(id);

        return buildBookViewModel(book);
    }

    public List<BookViewModel> findAll() {

        List<Book> books = bookRepo.findAll();
        List<BookViewModel> bvmList = new ArrayList<>();

        for(Book book : books){
            BookViewModel bookViewModel = buildBookViewModel(book);
            bvmList.add(bookViewModel);
        }

        return bvmList;

    }

    @Transactional
    public void update(BookViewModel bvm) {
        Book book = new Book();
        book.setId(bvm.getId());
        book.setTitle(bvm.getTitle());
        book.setAuthor(bvm.getAuthor());
        bookRepo.save(book);

    }

    public void deleteById(Integer id) {

        bookRepo.deleteById(id);

    }

    private BookViewModel buildBookViewModel(Book book){
        List<Note> nvmList = noteClient.getNotesByBookId(book.getId());

        BookViewModel bookViewModel = new BookViewModel();
        bookViewModel.setId(book.getId());
        bookViewModel.setTitle(book.getTitle());
        bookViewModel.setAuthor(book.getAuthor());
        bookViewModel.setNotes(noteClient.getNotesByBookId(book.getId()));

        return bookViewModel;

    }
}
