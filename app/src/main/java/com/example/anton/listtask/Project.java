package com.example.anton.listtask;

import java.util.List;

public class Project {
    public int id;
    private String title;
    private List<Todo> todos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }
}
