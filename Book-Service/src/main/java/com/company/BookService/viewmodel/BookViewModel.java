package com.company.BookService.viewmodel;

import java.util.List;

public class BookViewModel {

    private int id;
    private String title;
    private String author;
    private List<?> notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<?> getNotes() {
        return notes;
    }

    public void setNotes(List<?> notes) {
        this.notes = notes;
    }

}
