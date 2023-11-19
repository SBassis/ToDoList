package com.lib.todolist;

import java.io.Serializable;
import java.util.Date;

public class Task {

    public Task(String description, boolean status, Date date) {
        this.description = description;
        this.status = status;
        this.date = date;
    }

    private String description;
    private boolean status; //completed: true, false
    private Date date; //Due Date


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}
