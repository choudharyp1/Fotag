package com.example.p2choudh.fotagp2choudh.Model;

// Fotag is the model class of a single image/item.
public class Fotag {
    private int rating;
    private int id;

    Fotag(int rating, int id){
        this.rating = rating;
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
