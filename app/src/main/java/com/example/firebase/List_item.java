package com.example.firebase;

public class List_item {
    private String task;
    private String date;
    private boolean done;

    public List_item(String task, String date, boolean done) {
        this.task = task;
        this.done = done;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
