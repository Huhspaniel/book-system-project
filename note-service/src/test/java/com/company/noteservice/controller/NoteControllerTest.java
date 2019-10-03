package com.company.noteservice.controller;

import com.company.noteservice.dao.NoteRepository;
import com.company.noteservice.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.*;
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
        Note inputNote = new Note(1, "Note");

        String inputJSON = mapper.writeValueAsString(inputNote);

        Note outputNote = new Note(1, 1, "Note");

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
        Note inputNote = new Note(1, 1, "Note");

        String outputJson = mapper.writeValueAsString(inputNote);

        when(repo.findById(1)).thenReturn(Optional.of(inputNote));
        this.mockMvc.perform(get("/notes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getNotesByBook() throws Exception {
        Note note1 = new Note(1, "Note");

        Note note3 = new Note(1, "Post-it");

        List<Note> noteList = new ArrayList<>();
        noteList.add(note1);
        noteList.add(note3);

        when(repo.getByBookId(1)).thenReturn(noteList);

        String outputJson = mapper.writeValueAsString(noteList);

        this.mockMvc.perform(get("/notes/book/" + note1.getBookId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getAllNotes() throws Exception {
        Note note1 = new Note(1, "Note");

        Note note2 = new Note(2, "Letter");

        Note note3 = new Note(1, "Post-it");

        List<Note> noteList = new ArrayList<>();
        noteList.add(note1);
        noteList.add(note2);
        noteList.add(note3);

        when(repo.findAll()).thenReturn(noteList);

        String outputJson = mapper.writeValueAsString(noteList);

        this.mockMvc.perform(get("/notes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void updateNote() throws Exception {
        Note inputNote = new Note(1, "Note");
        Note expectedNote = new Note(1, 1, "Note");

        String inputJson = mapper.writeValueAsString(inputNote);

        this.mockMvc.perform(put("/notes/1")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(repo).save(expectedNote);
    }

    @Test
    public void deleteNote() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/notes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(repo).deleteById(1);
    }
}