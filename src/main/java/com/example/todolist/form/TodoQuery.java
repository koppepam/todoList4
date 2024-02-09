package com.example.todolist.form;

import lombok.Data;

@Data
public class TodoQuery {
    private String title;
    private Integer importance;
    private Integer urgency;
    private String deadlineFrom;
    private String deadlineTo;
    private String done;

    public TodoQuery() {
        title = "";
        importance = -1;
        urgency = -1;
        deadlineFrom = "";
        deadlineTo = "";
        done = "";
    }
}
