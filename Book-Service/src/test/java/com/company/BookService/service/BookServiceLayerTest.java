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
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceLayerTest {

    private BookServiceLayer bookServiceLayer;
    private BookRepository bookRepo;
    private NoteFeignClient feignClient;
    private RabbitTemplate rabbitTemplate;
    private static BookViewModel bvm;
    static {
        bvm = new BookViewModel(
                1,
                "Book Title",
                "Author Name",
                Arrays.asList(
                        new NoteViewModel(
                                1,
                                "super awesome note"
                        ),
                        new NoteViewModel(
                                2,
                                "note note note note asdf"
                        )
                )
        );
    }


    @Before
    public void setUp() {
        setUpBookServiceMock();
        setUpFeignClientMock();
        rabbitTemplate = mock(RabbitTemplate.class);

        bookServiceLayer = new BookServiceLayer(rabbitTemplate, bookRepo, feignClient);
    }

    @Test
    public void create() {
        BookViewModel bvmNoId = bvm.clone();
        bvmNoId.setId(null);
        ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);

        BookViewModel created = bookServiceLayer.create(bvm);

        assertNotNull(created);
        assertEquals(bvm, created);

        verify(rabbitTemplate, times(2)).convertAndSend(noteCaptor.capture());
        List<Note> expectedNotes = bvm.getNotes().stream()
                .map(nvm -> new Note(nvm.getId(), bvm.getId(), nvm.getNote()))
                .collect(Collectors.toList());
        List<Note> actualNotes = noteCaptor.getAllValues();

        assertEquals(expectedNotes, actualNotes);
    }

    @Test
    public void findById() {
        BookViewModel found = bookServiceLayer.findById(bvm.getId());

        assertEquals(bvm, found);
    }

    @Test
    public void findAll() {
        List<BookViewModel> expected = Collections.singletonList(bvm);

        List<BookViewModel> actual = bookServiceLayer.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void update() {
        ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);

        BookViewModel updateBvm = bvm.clone();
        updateBvm.setAuthor("new author");
        Book expectedBook = new Book(
                updateBvm.getId(),
                updateBvm.getTitle(),
                updateBvm.getAuthor()
        );

        List<NoteViewModel> updateNotes = new ArrayList<>(bvm.getNotes());
        updateNotes.get(0).setNote("Updated note!");
        updateNotes.get(1).setNote(null);
        updateNotes.add(new NoteViewModel("New note!"));
        updateBvm.setNotes(updateNotes);
        List<Note> expectedNotes = updateNotes.stream()
                .map(nvm -> new Note(nvm.getId(), bvm.getId(), nvm.getNote()))
                .collect(Collectors.toList());

        bookServiceLayer.update(updateBvm);

        verify(bookRepo).save(expectedBook);
        verify(rabbitTemplate, times(3)).convertAndSend(noteCaptor.capture());
        List<Note> actualNotes = noteCaptor.getAllValues();
        assertEquals(expectedNotes, actualNotes);
    }

    @Test
    public void deleteById() {
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(bookRepo).deleteById(integerCaptor.capture());
        int id = 1;

        bookServiceLayer.deleteById(id);

        verify(bookRepo, times(1)).deleteById(integerCaptor.getValue());
        assertEquals(id, integerCaptor.getValue().intValue());
    }

    private void setUpFeignClientMock(){
        feignClient = mock(NoteFeignClient.class);

        List<Note> notes = bvm.getNotes().stream()
                .map(nvm -> new Note(nvm.getId(), bvm.getId(), nvm.getNote()))
                .collect(Collectors.toList());

        doReturn(notes).when(feignClient).getNotesByBookId(bvm.getId());
    }

    private void setUpBookServiceMock(){
        bookRepo = mock(BookRepository.class);
        Book book = new Book(
                bvm.getId(),
                bvm.getTitle(),
                bvm.getAuthor()
        );
        Book bookNoId = book.clone();
        bookNoId.setId(null);
        List<Book> bookList = Collections.singletonList(book);

        doReturn(book).when(bookRepo).save(bookNoId);
        doReturn(book).when(bookRepo).getOne(book.getId());
        doReturn(Optional.of(book)).when(bookRepo).findById(book.getId());
        doReturn(true).when(bookRepo).existsById(book.getId());
        doReturn(bookList).when(bookRepo).findAll();
    }

}