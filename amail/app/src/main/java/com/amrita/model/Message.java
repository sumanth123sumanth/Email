package com.amrita.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message>{
    private int id;
    private String from;
    private String subject;
    private String message;
    private String timestamp;
    private String date;
    private boolean isImportant;
    private boolean isRead;
    private int color = -1;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
    Date d=new Date(),d2=new Date(),t=new Date(),t2=new Date();
    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int compareTo(Message o) {
        try {
             d=format.parse(this.getDate());
             d2=format.parse(o.getDate());
             t=dateFormat.parse(this.getTimestamp());
             t2=dateFormat.parse(o.getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
       if(d.after(d2) && t.after(t2))
       {
           return 1;
       }
        else{
            return -1;
       }
    }
}
