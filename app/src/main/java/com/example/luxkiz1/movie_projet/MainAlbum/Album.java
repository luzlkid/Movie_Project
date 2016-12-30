package com.example.luxkiz1.movie_projet.MainAlbum;

import com.parse.ParseFile;

/**
 * Created by luxkiz1 on 12/14/2016.
 */
public class Album {
    private String name;
    private ParseFile thumbnail;
    private String id;

    public Album() {
    }

    public Album(String name, ParseFile thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParseFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ParseFile thumbnail) {
        this.thumbnail = thumbnail;
    }
}
