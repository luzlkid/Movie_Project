package com.example.luxkiz1.movie_projet.Comment;


import android.widget.ImageView;

import com.parse.ParseFile;


public class CommentList {
    private String username;
    private String comment;
    private ParseFile photo;

    public CommentList(){}
    public CommentList(String username, String comment) {
        this.username = username;
        this.comment = comment;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ParseFile getPhoto() {
        return photo;
    }

    public void setPhoto(ParseFile photo) {
        this.photo = photo;
    }
}
