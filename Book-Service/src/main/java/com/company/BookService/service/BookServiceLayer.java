package com.company.BookService.service;

import com.company.BookService.dao.BookRepository;
import com.company.BookService.util.feign.NoteFeignClient;
import com.company.BookService.viewmodel.BookViewModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookServiceLayer {
    private static final String EXCHANGE = "";
    private static final String ROUTING_KEY = "";
    private RabbitTemplate rabbit;
    private BookRepository bookRepo;
    private NoteFeignClient noteClient;

    @Autowired
    public BookServiceLayer(RabbitTemplate rabbit, BookRepository bookRepo, NoteFeignClient noteClient) {
        this.rabbit = rabbit;
        this.bookRepo = bookRepo;
        this.noteClient = noteClient;
    }

    public BookViewModel create(BookViewModel bvm) {
        return null;
    }

    public Optional<BookViewModel> findById(Integer id) {
        return Optional.empty();
    }

    public List<BookViewModel> findAll() {
        return Collections.emptyList();
    }

    public void update(BookViewModel bvm) {

    }

    public void deleteById(Integer id) {

    }
}
