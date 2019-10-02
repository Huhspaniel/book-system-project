package com.company.BookService.util.feign;

import com.company.BookService.dto.Note;
import com.company.BookService.viewmodel.NoteViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient
public interface NoteFeignClient {
    @GetMapping("/notes/book/{id}")
    List<Note> getNotesByBookId(@PathVariable Integer id);
}
