package com.company.BookService.service;

import com.company.BookService.dao.BookRepository;
import com.company.BookService.dto.Book;
import com.company.BookService.dto.Note;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.util.messages.DeleteNoteMsg;
import com.company.BookService.viewmodel.BookViewModel;
import com.company.BookService.viewmodel.NoteViewModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookServiceLayer {
    private static final String EXCHANGE = "note-exchange";
    private static final String ROUTING_KEY = "note.bookService";
    private RabbitTemplate rabbit;
    private BookRepository bookRepo;
    private NoteFeignClient noteClient;


    @Autowired
    public BookServiceLayer(RabbitTemplate rabbit, BookRepository bookRepo, NoteFeignClient noteClient) {
        rabbit.setRoutingKey(ROUTING_KEY);
        rabbit.setExchange(EXCHANGE);
        this.rabbit = rabbit;
        this.bookRepo = bookRepo;
        this.noteClient = noteClient;
    }

    @Transactional
    public BookViewModel create(BookViewModel bvm) {
        Book book = new Book();
        book.setTitle(bvm.getTitle());
        book.setAuthor(bvm.getAuthor());
        book = bookRepo.save(book);
        Integer bookId = book.getId();
        bvm.setId(bookId);
        bvm.getNotes().stream()
                .map(nvm -> new Note(nvm.getId(), bookId, nvm.getNote()))
                //sends to broker
                .forEach(rabbit::convertAndSend);

        return bvm;
    }

    public BookViewModel findById(Integer id) {
        Book book = bookRepo.getOne(id);

        return buildBookViewModel(book);
    }

    public List<BookViewModel> findAll() {

        List<Book> books = bookRepo.findAll();
        List<BookViewModel> bvmList = new ArrayList<>();

        for (Book book : books) {
            BookViewModel bookViewModel = buildBookViewModel(book);
            bvmList.add(bookViewModel);
        }

        return bvmList;

    }

    @Transactional
    public void update(BookViewModel bvm) {
        Integer bookId = bvm.getId();
        if (bookRepo.existsById(bookId)) {
            Book book = new Book();
            book.setId(bookId);
            book.setTitle(bvm.getTitle());
            book.setAuthor(bvm.getAuthor());
            bookRepo.save(book);

            bvm.getNotes().stream()
                    .map(nvm -> new Note(nvm.getId(), bookId, nvm.getNote()))
                    .forEach(rabbit::convertAndSend);
        }
    }

    public void deleteById(Integer id) {
        bookRepo.deleteById(id);
        noteClient.getNotesByBookId(id).stream()
                .map(DeleteNoteMsg::of)
                .forEach(rabbit::convertAndSend);
    }


    private BookViewModel buildBookViewModel(Book book) {
        BookViewModel bookViewModel = new BookViewModel();
        bookViewModel.setId(book.getId());
        bookViewModel.setTitle(book.getTitle());
        bookViewModel.setAuthor(book.getAuthor());
        bookViewModel.setNotes(noteClient
                .getNotesByBookId(book.getId()).stream()
                .map(this::buildNoteViewModel)
                .collect(Collectors.toList())
        );

        return bookViewModel;
    }

    private NoteViewModel buildNoteViewModel(Note note) {
        return new NoteViewModel(
                note.getNoteId(),
                note.getNote()
        );
    }
}
