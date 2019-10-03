package com.company.noteservice.controller;

import com.company.noteservice.dao.NoteRepository;
import com.company.noteservice.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NoteController.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteRepository repo;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        reset(repo);
    }

    @Test
    public void createNote() throws Exception {
        Note inputNote = new Note();
        inputNote.setBookId(1);
        inputNote.setNote("Note");

        String inputJSON = mapper.writeValueAsString(inputNote);

        Note outputNote = new Note();
        outputNote.setBookId(1);
        outputNote.setNote("Note");
        outputNote.setNoteId(1);

        String outputJson = mapper.writeValueAsString(outputNote);

        when(repo.save(inputNote)).thenReturn(outputNote);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/notes/")
                .content(inputJSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getNote() throws Exception {
        Note inputNote = new Note();
        inputNote.setNoteId(1);
        inputNote.setBookId(1);
        inputNote.setNote("Note");

        String outputJson = mapper.writeValueAsString(inputNote);

        when(repo.findById(1)).thenReturn(Optional.of(inputNote));
        this.mockMvc.perform(get("/notes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getNotesByBook() throws Exception {
        Note inputNote = new Note();
        inputNote.setBookId(1);
        inputNote.setNote("Note");

        Note note2 = new Note();
        note2.setBookId(2);
        note2.setNote("Letter");
        repo.save(note2);

        Note note3 = new Note();
        note3.setBookId(1);
        note3.setNote("Post-it");
        repo.save(note3);

        List<Note> noteList = new ArrayList<>();
        noteList.add(inputNote);
        noteList.add(note3);

        when(repo.getByBookId(1)).thenReturn(noteList);

        List<Note> listChecker = new ArrayList<>();
        listChecker.add(inputNote);
        listChecker.add(note3);
        String outputJson = mapper.writeValueAsString(listChecker);

        this.mockMvc.perform(get("/notes/book/" + inputNote.getBookId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getAllNotes() throws Exception {
        Note inputNote = new Note();
        inputNote.setBookId(1);
        inputNote.setNote("Note");

        Note note2 = new Note();
        note2.setBookId(2);
        note2.setNote("Letter");
        repo.save(note2);

        Note note3 = new Note();
        note3.setBookId(1);
        note3.setNote("Post-it");
        repo.save(note3);

        List<Note> noteList = new ArrayList<>();
        noteList.add(inputNote);
        noteList.add(note2);
        noteList.add(note3);

        when(repo.findAll()).thenReturn(noteList);

        List<Note> listChecker = new ArrayList<>();
        listChecker.add(inputNote);
        listChecker.add(note2);
        listChecker.add(note3);
        String outputJson = mapper.writeValueAsString(listChecker);

        this.mockMvc.perform(get("/notes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void updateNote() throws Exception {
        Note inputNote = new Note();
        inputNote.setBookId(1);
        inputNote.setNote("Note");

        String inputJson = mapper.writeValueAsString(inputNote);

        this.mockMvc.perform(put("/notes/1")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void deleteNote() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/notes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}