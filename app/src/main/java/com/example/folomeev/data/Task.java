package com.example.folomeev.data;

public class Task {
    public int id;
    public String text;
    public boolean isDone;

    public Task(int id, String text, int isDone) {
        this.id = id;
        this.text = text;
        this.isDone = (isDone == 1);
    }
}