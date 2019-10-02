package com.company.BookService.viewmodel;

import java.util.Objects;

public class NoteViewModel implements Cloneable {
    private Integer id;
    private String note;

    public NoteViewModel() {
    }

    public NoteViewModel(Integer id, String note) {
        this.id = id;
        this.note = note;
    }

    public NoteViewModel(String note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteViewModel that = (NoteViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note);
    }

    @Override
    public String toString() {
        return "NoteViewModel{" +
                "id=" + id +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public NoteViewModel clone() {
        try {
            return (NoteViewModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
