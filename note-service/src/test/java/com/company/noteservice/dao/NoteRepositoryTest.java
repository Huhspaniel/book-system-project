package com.company.noteservice.dao;

import com.company.noteservice.model.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NoteRepositoryTest {

    @Autowired
    NoteRepository repo;

    @Test
    public void getAdd(){
        repo.deleteAll();
        Note note = new Note();
        note.setBookId(1);
        note.setNote("Note");
        repo.save(note);

        Note note2 = repo.findById(note.getNoteId()).orElse(null);
        assertEquals(note, note2);
    }

    @Test
    public void deleteTest(){
        repo.deleteAll();
        Note note = new Note();
        note.setBookId(1);
        note.setNote("Note");
        repo.save(note);

        repo.deleteById(note.getNoteId());
        Note note2 = repo.findById(note.getNoteId()).orElse(null);
        assertNull(note2);
    }

    @Test
    public void updateTest(){
        repo.deleteAll();
        Note note = new Note();
        note.setBookId(1);
        note.setNote("Note");
        repo.save(note);

        note.setBookId(2);
        note.setNote("Letter");
        repo.save(note);

        Note note2 = repo.findById(note.getNoteId()).orElse(null);
        assertEquals(note, note2);
    }

    @Test
    public void findAllTest(){
        repo.deleteAll();
        Note note = new Note();
        note.setBookId(1);
        note.setNote("Note");
        repo.save(note);

        Note note2 = new Note();
        note2.setBookId(2);
        note2.setNote("Letter");
        repo.save(note2);

        List<Note> noteList = repo.findAll();
        assertEquals(2,noteList.size());
    }

    @Test
    public void getByBookId() {
        repo.deleteAll();
        Note note = new Note();
        note.setBookId(1);
        note.setNote("Note");
        repo.save(note);

        Note note2 = new Note();
        note2.setBookId(2);
        note2.setNote("Letter");
        repo.save(note2);

        Note note3 = new Note();
        note3.setBookId(1);
        note3.setNote("Post-it");
        repo.save(note3);

        List<Note> noteList = repo.getByBookId(1);
        assertEquals(2,noteList.size());
        List<Note> noteList1 = repo.getByBookId(2);
        assertEquals(1,noteList1.size());

    }
}