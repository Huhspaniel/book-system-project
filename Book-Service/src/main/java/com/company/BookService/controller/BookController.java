package com.company.BookService.controller;

import com.company.BookService.service.BookServiceLayer;
import com.company.BookService.viewmodel.BookViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookServiceLayer service;

    @Autowired
    public BookController(BookServiceLayer service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookViewModel create(BookViewModel book) {
        return null;
    }

    @GetMapping("/{id}")
    public BookViewModel getById(@PathVariable Integer id) {
        return null;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookViewModel> getAll() {
        return Collections.emptyList();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody BookViewModel bookViewModel) {

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {

    }
}
