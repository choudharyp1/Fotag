package com.example.p2choudh.fotagp2choudh.Model;

//FotagModel is the main model class of this application.
//Holds all the Fotags, and decides what needs to be displayed.

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.p2choudh.fotagp2choudh.View.IView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FotagModel {

    private int filterRating;
    private boolean isGrid = false;

    private ArrayList <Fotag> allFotags;
    private ArrayList<Fotag> activeFotags;
    private ArrayList <IView> views;
    private HashMap<Integer, Bitmap> cache;

    private ArrayList <String> imageURLs;

    private static int IMAGE_ID_COUNTER = 0;



    //constructor
    public FotagModel(){
        filterRating = 0;
        isGrid = false;
        allFotags = new ArrayList<Fotag>();
        activeFotags = new ArrayList<Fotag>();
        views = new ArrayList<IView>();
        cache = new HashMap<Integer, Bitmap>();
        imageURLs = new ArrayList<String>();
        addURLsToList();
        IMAGE_ID_COUNTER = 0;
    }

    public void addURLsToList(){
        String url = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images";
        ArrayList <String> imgNames = new ArrayList<String>();
        imgNames.add("bunny.jpg");
        imgNames.add("chinchilla.jpg");
        imgNames.add("doggo.jpg");
        imgNames.add("hamster.jpg");
        imgNames.add("husky.jpg");
        imgNames.add("kitten.png");
        imgNames.add("loris.jpg");
        imgNames.add("puppy.jpg");
        imgNames.add("redpanda.jpg");
        imgNames.add("sleepy.png");

        for (int i = 0; i < imgNames.size(); i++){
            imageURLs.add(url + "/" + imgNames.get(i));
        }
    }

    //getters.

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    public boolean isGrid() {
        return isGrid;
    }

    public int getFilterRating() {
        return filterRating;
    }

    public ArrayList<Fotag> getActiveFotags() {
        return activeFotags;
    }

    public ArrayList<Fotag> getAllFotags(){
        return allFotags;
    }

    public HashMap<Integer, Bitmap> getCache() {
        return cache;
    }

    //setters

    public void setGrid(boolean grid) {
        isGrid = grid;
        updateAllViews();
    }

    public void setFilterRating(int filterRating) {
        this.filterRating = filterRating;
        updateActiveFotags();
        updateAllViews();
    }

    public void loadImage(ArrayList<Bitmap> bmap){
        for(int i = 0; i < bmap.size(); i++){
            Bitmap bp = bmap.get(i);
            allFotags.add(new Fotag(0, IMAGE_ID_COUNTER));
            cache.put(IMAGE_ID_COUNTER, bp);
            IMAGE_ID_COUNTER--;
            updateActiveFotags();
            updateAllViews();
        }
    }

    public void clearFotags(){
        allFotags.clear();
        activeFotags.clear();
        cache.clear();
        updateAllViews();
    }

    public void updateActiveFotags() {
        activeFotags.clear();
        for (int i = 0; i < allFotags.size(); i++) {
            if (allFotags.get(i).getRating() >= filterRating) {
                activeFotags.add(allFotags.get(i));
            }
        }
    }

    public void setActiveFotagRating(int id, int rate){
        activeFotags.get(id).setRating(rate);
        if (rate < filterRating){
            updateActiveFotags();
            updateAllViews();
        }

    }

    public void clearActiveFotagRating(int id){
        activeFotags.get(id).setRating(0);
        if (filterRating > 0){
            updateActiveFotags();
            updateAllViews();
        }
    }

    public void addView(IView view){
        views.add(view);
    }

    public void updateAllViews(){
        for (IView view : views){
            view.updateView();
        }
    }

}
