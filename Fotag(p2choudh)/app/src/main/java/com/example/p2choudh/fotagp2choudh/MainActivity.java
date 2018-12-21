package com.example.p2choudh.fotagp2choudh;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RatingBar;

import com.example.p2choudh.fotagp2choudh.Model.FotagAdapter;
import com.example.p2choudh.fotagp2choudh.Model.FotagModel;
import com.example.p2choudh.fotagp2choudh.View.IView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements IView {

    //Model

    private static FotagModel fotagModel;
    //Tool bar objects
    private Button loadbtn;
    private Button clearImgbtn;
    private Button resetRatebtn;
    private RatingBar ratingBar;

    //Grid objects.
    private GridView gridView;
    private ArrayList<Bitmap> bmap;
    private FotagAdapter fotagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotagModel = new FotagModel();
        fotagModel.addView(this);
        loadbtn = (Button) findViewById(R.id.load_btn);
        clearImgbtn = (Button) findViewById(R.id.clear_img);
        resetRatebtn = (Button) findViewById(R.id.reset_rate);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //Grid content
        gridView = (GridView) findViewById(R.id.gridview);
        fotagAdapter = new FotagAdapter(fotagModel, this);
        gridView.setNumColumns(1);
        gridView.setAdapter(fotagAdapter);

        bmap = new ArrayList<Bitmap>();

        //Button listeners
        loadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageFromUrl(fotagModel.getImageURLs());
            }
        });

        clearImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotagModel.clearFotags();
            }
        });

        resetRatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotagModel.setFilterRating(0);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int newRate = (int) v;
                fotagModel.setFilterRating(newRate);
            }
        });
    }

    public static FotagModel getFotagModel(){
        return fotagModel;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void updateView(){
        this.ratingBar.setRating((float) fotagModel.getFilterRating());
        this.fotagAdapter.notifyDataSetChanged();
        this.gridView.setAdapter(this.fotagAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fotagModel.setGrid(true);
            gridView.setNumColumns(2);
            this.fotagAdapter.notifyDataSetChanged();
            this.fotagAdapter = new FotagAdapter(fotagModel, this);
            this.gridView.setAdapter(this.fotagAdapter);
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            fotagModel.setGrid(false);
            gridView.setNumColumns(1);
            this.fotagAdapter.notifyDataSetChanged();
            this.fotagAdapter = new FotagAdapter(fotagModel, this);
            this.gridView.setAdapter(this.fotagAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position = data.getIntExtra("position", 0);
        gridView.setSelection(position);
        updateView();
    }

    //Loading an image from the internet using a url.

    public void loadImageFromUrl(final ArrayList<String> urlArray){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                fotagModel.clearFotags();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    for (int i = 0; i < urlArray.size(); i++){
                        String url = urlArray.get(i);
                        String uri;
                        if (!url.startsWith("http://") && !url.startsWith("https://")){
                            uri = "http://" + url;
                        }else{
                            uri = url;
                            InputStream in = new java.net.URL(url).openStream();
                            bmap.add(BitmapFactory.decodeStream(in));
                        }
                    }
                }catch (IOException e){
                    Log.d("Main Activity", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (bmap.size() != 0){
                    fotagModel.loadImage(bmap);
                    bmap.clear();
                }
                fotagModel.updateActiveFotags();
                fotagModel.updateAllViews();
            }
        }.execute();
    }

    public static int getSampleSize(int oriWidth, int oriHeight, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        if (oriHeight > reqHeight || oriWidth > reqWidth) {
            final int halfHeight = oriHeight / 2;
            final int halfWidth = oriWidth / 2;
            while ((halfHeight / sampleSize) > reqHeight && (halfWidth / sampleSize) > reqWidth)
                sampleSize *= 2;
        }
        return sampleSize;
    }
}
