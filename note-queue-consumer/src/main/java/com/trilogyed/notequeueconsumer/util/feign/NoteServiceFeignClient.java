package com.trilogyed.notequeueconsumer.util.feign;

import com.trilogyed.notequeueconsumer.util.messages.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient
public interface NoteServiceFeignClient {
    @PostMapping("/notes")
    Note createNote(@RequestBody Note note);

    @PutMapping("/notes/{id}")
    void updateNote(@PathVariable Integer id, @RequestBody Note note);
}
