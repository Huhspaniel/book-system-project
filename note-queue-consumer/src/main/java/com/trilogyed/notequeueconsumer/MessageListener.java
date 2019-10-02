package com.trilogyed.notequeueconsumer;

import com.trilogyed.notequeueconsumer.util.feign.NoteServiceFeignClient;
import com.trilogyed.notequeueconsumer.util.messages.Note;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    private NoteServiceFeignClient noteClient;

    @Autowired
    public MessageListener(NoteServiceFeignClient noteClient) {
        this.noteClient = noteClient;
    }

    @RabbitListener(queues = NoteQueueConsumerApplication.QUEUE_NAME)
    public void receiveMessage(Note note) {
        if (note.getNoteId() == null) {
            noteClient.createNote(note);
        } else if (note.getNote() == null) {
            noteClient.deleteNote(note.getNoteId());
        } else {
            noteClient.updateNote(note.getNoteId(), note);
        }
    }
}
