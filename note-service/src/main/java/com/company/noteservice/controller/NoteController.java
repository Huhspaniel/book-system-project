package com.company.noteservice.controller;

import com.company.noteservice.dao.NoteRepository;
import com.company.noteservice.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class NoteController {
    @Autowired
    NoteRepository repo;

    public void NoteController(NoteRepository repo){
        this.repo = repo;
    }

    @RequestMapping(value ="/notes", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@RequestBody @Valid Note note){
        return repo.save(note);
    }

    @RequestMapping(value ="/notes/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Note getNote(@PathVariable("id") int id){
            return repo.findById(id).orElse(null);
    }

    @RequestMapping(value ="/notes/book/{book_id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Note> getNotesByBook(@PathVariable("book_id") int id){
        return repo.getByBookId(id);
    }

    @RequestMapping(value ="/notes", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Note> getAllNotes(){
        return repo.findAll();
    }

    @RequestMapping(value ="/notes/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateNote(@RequestBody @Valid Note note, @PathVariable("id") int id){
        note.setNoteId(id);
        repo.save(note);
    }

    @RequestMapping(value ="/notes/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteNote(@PathVariable(name="id") int id){
        repo.deleteById(id);
    }
}
