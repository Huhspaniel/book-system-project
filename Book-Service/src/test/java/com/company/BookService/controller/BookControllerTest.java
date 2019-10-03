package com.company.BookService.controller;

import com.company.BookService.service.BookServiceLayer;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.viewmodel.BookViewModel;
import com.company.BookService.viewmodel.NoteViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BookServiceLayer bookService;

    @MockBean
    RabbitTemplate rabbitTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void create() throws Exception {

        BookViewModel bvm = new BookViewModel();
        bvm.setTitle("New Title");
        bvm.setAuthor("Author");

            NoteViewModel note = new NoteViewModel();
            note.setId(2);
            note.setNote("notenotenote");

            NoteViewModel note2 = new NoteViewModel();
            note2.setId(3);
            note2.setNote("notenotenote");

            List<NoteViewModel> notes = new ArrayList<>();
            notes.add(note);
            notes.add(note2);

        bvm.setNotes(notes);;

        String inputJson = mapper.writeValueAsString(bvm);

        BookViewModel bvm2 = new BookViewModel();
        bvm2.setTitle("New Title");
        bvm2.setAuthor("Author");
        bvm2.setNotes(notes);
        bvm2.setId(2);

        String outputJson = mapper.writeValueAsString(bvm2);

        when(bookService.create(bvm)).thenReturn(bvm2);

        this.mockMvc.perform(post("/books")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
//                .andExpect(content().json(outputJson));

    }

    @Test
    public void getById() throws Exception {

        BookViewModel bvm = new BookViewModel();
        bvm.setTitle("New Title");
        bvm.setAuthor("Author");

        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        bvm.setNotes(notes);;
        bvm.setId(2);

        String outputJson = mapper.writeValueAsString(bvm);

        when(bookService.findById(2)).thenReturn(bvm);

        this.mockMvc.perform(get("/books/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

    }

    @Test
    public void getAll() throws Exception {

        BookViewModel bvm = new BookViewModel();
        bvm.setTitle("New Title");
        bvm.setAuthor("Author");
        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        bvm.setNotes(notes);;
        bvm.setId(1);

        BookViewModel bvm2 = new BookViewModel();
        bvm2.setTitle("New Title2");
        bvm2.setAuthor("Author2");
        NoteViewModel note3 = new NoteViewModel();
        note3.setId(4);
        note3.setNote("notenotenote2");

        NoteViewModel note4 = new NoteViewModel();
        note4.setId(5);
        note4.setNote("notenotenote3");

        List<NoteViewModel> notes2 = new ArrayList<>();
        notes2.add(note3);
        notes2.add(note4);

        bvm2.setNotes(notes2);
        bvm2.setId(2);

        List<BookViewModel> bookViewModels = new ArrayList<>();
        bookViewModels.add(bvm);
        bookViewModels.add(bvm2);

        when(bookService.findAll()).thenReturn(bookViewModels);

        List<BookViewModel> bookViewModels1 = new ArrayList<>();
        bookViewModels1.addAll(bookViewModels);

        String outputJson = mapper.writeValueAsString(bookViewModels1);

        this.mockMvc.perform(get("/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));

    }

    @Test
    public void update() throws Exception {

        BookViewModel bvm = new BookViewModel();
        bvm.setTitle("New Title");
        bvm.setAuthor("Author");

        NoteViewModel note = new NoteViewModel();
        note.setId(2);
        note.setNote("notenotenote");

        NoteViewModel note2 = new NoteViewModel();
        note2.setId(3);
        note2.setNote("notenotenote");

        List<NoteViewModel> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);

        bvm.setNotes(notes);;
        bvm.setId(2);

        String inputJson = mapper.writeValueAsString(bvm);

        this.mockMvc.perform(put("/books")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    public void deleteById() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/books/2"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

    }
}