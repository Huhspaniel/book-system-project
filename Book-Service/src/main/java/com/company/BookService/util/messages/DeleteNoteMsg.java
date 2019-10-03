package com.company.BookService.util.messages;

import com.company.BookService.dto.Note;

public class DeleteNoteMsg extends Note {
    private DeleteNoteMsg(Integer noteId) {
        super();
        this.setNoteId(noteId);
    }
    public static Note of(Note n) {
        return new DeleteNoteMsg(n.getNoteId());
    }
}
