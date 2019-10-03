package com.company.BookService.service;

import com.company.BookService.dao.BookRepository;
import com.company.BookService.dto.Book;
import com.company.BookService.dto.Note;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.viewmodel.BookViewModel;
import com.company.BookService.viewmodel.NoteViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceLayerTest {

    private BookServiceLayer bookServiceLayer;
    private BookRepository bookRepo;
    private RabbitTemplate rabbitTemplate;
    private NoteFeignClient feignClient;


    @Before
    public void setUp() throws Exception {

        setUpBookServiceMock();
        setUpFeignClientMock();
        rabbitTemplate = mock(RabbitTemplate.class);

        bookServiceLayer = new BookServiceLayer(rabbitTemplate, bookRepo, feignClient);

    }

    @Test
    public void create() {

        BookViewModel book = new BookViewModel();
        book.setId(1);
        book.setTitle("Book Title");
        book.setAuthor("Author Name");


        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        book.setNotes(notes);
        book = bookServiceLayer.create(book);

        BookViewModel fromService = bookServiceLayer.findById(book.getId());

        assertEquals(book, fromService);

    }

    @Test
    public void findById() {

        BookViewModel book = new BookViewModel();
        book.setId(1);
        book.setTitle("Book Title");
        book.setAuthor("Author Name");


        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        book.setNotes(notes);
        book = bookServiceLayer.create(book);

        BookViewModel fromService = bookServiceLayer.findById(book.getId());

        assertEquals(fromService.getId(), book.getId());

    }

    @Test
    public void findAll() {

        BookViewModel book = new BookViewModel();
        book.setId(1);
        book.setTitle("Book Title");
        book.setAuthor("Author Name");

        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        book.setNotes(notes);
        book = bookServiceLayer.create(book);


        List<BookViewModel> books = bookServiceLayer.findAll();
        assertEquals(1, books.size());

    }

    @Test
    public void update() {

        BookViewModel book = new BookViewModel();
        book.setId(1);
        book.setTitle("Book Title");
        book.setAuthor("Author Name");


        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        book.setNotes(notes);
        book = bookServiceLayer.create(book);

        book.setTitle("New Title");
        bookServiceLayer.update(book);

        BookViewModel fromService = bookServiceLayer.findById(book.getId());
        assertEquals("New Title", book.getTitle());



    }

    @Test
    public void deleteById() {

        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(bookRepo).deleteById(integerCaptor.capture());

        bookServiceLayer.deleteById(1);
        verify(bookRepo, times(1)).deleteById(integerCaptor.getValue());
        assertEquals(1, integerCaptor.getValue().intValue());

    }

    private void setUpFeignClientMock(){
        feignClient = mock(NoteFeignClient.class);

        Note note = new Note();
        note.setNoteId(2);
        note.setBookId(1);
        note.setNote("notenotenote");

        Note note2 = new Note();
        note2.setNoteId(3);
        note2.setBookId(1);
        note2.setNote("notenotenote");

        List<Note> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        doReturn(notes).when(feignClient).getNotesByBookId(note.getBookId());
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