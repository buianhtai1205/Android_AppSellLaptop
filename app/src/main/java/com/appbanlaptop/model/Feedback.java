package com.appbanlaptop.model;

public class Feedback {
    private int id;
    private int star;
    private String comment;
    private String user_fullname;
    private String user_avatar;

    public Feedback(int id, int star, String comment, String user_fullname, String user_avatar) {
        this.id = id;
        this.star = star;
        this.comment = comment;
        this.user_fullname = user_fullname;
        this.user_avatar = user_avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }
}
