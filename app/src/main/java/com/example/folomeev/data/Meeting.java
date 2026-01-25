package com.example.folomeev.data;

public class Meeting {
    private int id;
    private String title;
    private String category;
    private String date;

    public Meeting(String title, String category, String date){
        this.title = title;
        this.category = category;
        this.date = date;
    }
    public Meeting(int id, String title, String category, String date){
        this.id = id;
        this.title = title;
        this.category = category;
        this.date = date;
    }

    public int getId() {return id;}
    public String getTitle() {return title;}

    public String getCategory() {return category;}
    public String getDate() {return date;}


}
