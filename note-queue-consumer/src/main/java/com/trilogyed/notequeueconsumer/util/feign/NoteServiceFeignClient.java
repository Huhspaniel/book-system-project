package com.trilogyed.notequeueconsumer.util.feign;

import com.trilogyed.notequeueconsumer.util.messages.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:1984", name = "note-service")
public interface NoteServiceFeignClient {
    @PostMapping("/notes")
    Note createNote(@RequestBody Note note);

    @PutMapping("/notes/{id}")
    void updateNote(@PathVariable Integer id, @RequestBody Note note);

    @DeleteMapping("/notes/{id}")
    void deleteNote(@PathVariable Integer id);
}
