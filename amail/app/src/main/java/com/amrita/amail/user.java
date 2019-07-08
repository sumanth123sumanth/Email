package com.amrita.amail;

import java.util.List;

public class user {
    private String username;
    private String password;
    private String dob;
    private String address;
    private String phno;
    private String name;
    List<email> inbox;
    private String uid;
    List<contact> contactList;
    public List<email> getInbox() {
        return inbox;
    }

    public void setInbox(List<email> inbox) {
        this.inbox = inbox;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public user(String username, String password, String dob, String address, String phno, String name, String uid) {

        this.username = username;
        this.password = password;
        this.dob = dob;
        this.address = address;
        this.phno = phno;
        this.name = name;
        this.inbox = inbox;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public user(String username, String password, String dob, String address, String phno, String name) {

        this.username = username;
        this.password = password;
        this.dob = dob;
        this.address = address;
        this.phno = phno;
        this.name = name;
    }
}
