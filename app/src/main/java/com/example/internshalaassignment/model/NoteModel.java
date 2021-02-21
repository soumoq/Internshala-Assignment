package com.example.internshalaassignment.model;

public class NoteModel {
    private int noteId;
    private String name;
    private String note;

    public NoteModel(int noteId, String name, String note) {
        this.noteId = noteId;
        this.name = name;
        this.note = note;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }
}
