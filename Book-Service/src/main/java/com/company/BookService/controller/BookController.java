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

    @Autowired
    BookServiceLayer bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookViewModel create(@RequestBody BookViewModel book) {

        return bookService.create(book);
    }

    @GetMapping("/{id}")
    public BookViewModel getById(@PathVariable Integer id) {

        return bookService.findById(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookViewModel> getAll() {

        return bookService.findAll();

    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody BookViewModel bookViewModel) {


        bookService.update(bookViewModel);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        bookService.deleteById(id);
    }
}
