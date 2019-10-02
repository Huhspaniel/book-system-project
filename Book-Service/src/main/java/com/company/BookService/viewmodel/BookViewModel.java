package com.company.BookService.viewmodel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookViewModel implements Cloneable {

    @NotNull
    private Integer id;
    @NotEmpty
    @Size(max = 50)
    private String title;
    @NotEmpty
    @Size(max = 50)
    private String author;
    private List<?> notes;

    public BookViewModel() {
    }

    public BookViewModel(@NotNull Integer id, @NotEmpty @Size(max = 50) String title, @NotEmpty @Size(max = 50) String author, List<?> notes) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.notes = notes;
    }

    public BookViewModel(@NotEmpty @Size(max = 50) String title, @NotEmpty @Size(max = 50) String author, List<?> notes) {
        this.title = title;
        this.author = author;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookViewModel that = (BookViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(author, that.author) &&
                Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, notes);
    }

    @Override
    public String toString() {
        return "BookViewModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", notes=" + notes +
                '}';
    }

    @Override
    public BookViewModel clone() {
        try {
            return (BookViewModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
