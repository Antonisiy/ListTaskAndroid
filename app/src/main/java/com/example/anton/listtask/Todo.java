package com.example.anton.listtask;

public class Todo {
    public int id;
    private String text;
    private Boolean isCompleted;
    private int project_id;

    Todo(String text)
    {
        this.text = text;
        isCompleted = false;
    }


    public String getText() {
        return text;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return getText();
    }
}
