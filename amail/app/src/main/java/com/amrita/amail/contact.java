package com.amrita.amail;

public class contact {
    private String name;
    private String phno;
    private String uid;
    private String amail;

    public String getAmail() {
        return amail;
    }

    public void setAmail(String amail) {
        this.amail = amail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public contact(String name, String phno,String uid,String amail) {

        this.name = name;
        this.phno = phno;
        this.uid=uid;
        this.amail=amail;
    }
}
