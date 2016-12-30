package com.example.luxkiz1.movie_projet.Favorite;


import com.parse.ParseFile;

public class Favo {
    private String titlefavo;
    private ParseFile thumbfavo;
    private String tacgiafavo;
    private String id;



    public Favo(){

    }
    public Favo(String titlefavo, String tacgiafavo){
        this.titlefavo = titlefavo;
        this.tacgiafavo = tacgiafavo;
    }

    public String getTacgiafavo() {
        return tacgiafavo;
    }

    public void setTacgiafavo(String tacgiafavo) {
        this.tacgiafavo = tacgiafavo;
    }

    public String getTitlefavo() {
        return titlefavo;
    }

    public void setTitlefavo(String titlefavo) {
        this.titlefavo = titlefavo;
    }

    public ParseFile getThumbfavo() {
        return thumbfavo;
    }

    public void setThumbfavo(ParseFile thumbfavo) {
        this.thumbfavo = thumbfavo;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
