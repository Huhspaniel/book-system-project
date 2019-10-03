package com.company.BookService.service;

import com.company.BookService.dao.BookRepository;
import com.company.BookService.dto.Book;
import com.company.BookService.dto.Note;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.viewmodel.BookViewModel;
import com.company.BookService.viewmodel.NoteViewModel;
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

    private List<NoteViewModel> findByBookId(int bookId){
        List<Note> notes = noteClient.getNotesByBookId(bookId);
        List<NoteViewModel> noteViewModels = new ArrayList<>();

        for(Note note : notes){
            NoteViewModel nvm = buildNoteViewModel(note);
            noteViewModels.add(nvm);
        }

        return noteViewModels;
    }

    private NoteViewModel buildNoteViewModel(Note note){
        Book book = bookRepo.getOne(note.getBookId());

        NoteViewModel nvm = new NoteViewModel();
        nvm.setId(note.getNoteId());
        nvm.setNote(note.getNote());

        return nvm;
    }

    @Transactional
    public BookViewModel create(BookViewModel bvm) {
        Book book = new Book();
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
        List<NoteViewModel> noteViewModels = findByBookId(book.getId());

        BookViewModel bookViewModel = new BookViewModel();
        bookViewModel.setId(book.getId());
        bookViewModel.setTitle(book.getTitle());
        bookViewModel.setAuthor(book.getAuthor());
        bookViewModel.setNotes(noteViewModels);

        return bookViewModel;

    }
}
