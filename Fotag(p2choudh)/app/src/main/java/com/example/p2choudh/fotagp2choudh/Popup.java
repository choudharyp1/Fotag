package com.example.p2choudh.fotagp2choudh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.p2choudh.fotagp2choudh.Model.FotagModel;
import com.example.p2choudh.fotagp2choudh.View.IView;

public class Popup extends Activity  {
    private FotagModel fotagModel;
    private Button resetbtn;
    private RatingBar ratingBar;
    private ImageView imageView;
    private int height;
    private int width;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up);

        fotagModel = MainActivity.getFotagModel();

        resetbtn = (Button) findViewById(R.id.fullImg_RateReset);
        ratingBar = (RatingBar) findViewById(R.id.fullImg_Rate);
        imageView = (ImageView) findViewById(R.id.fullImg);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        if (fotagModel.isGrid()){
            width = screenWidth/2;
            height = screenHeight/2;
        }else{
            width = screenWidth;
            height = screenHeight/3;
        }
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        Intent int1 = new Intent(this, MainActivity.class);
        int1.putExtra("position", position);
        this.setResult(1,int1);
        Bitmap bp = fotagModel.getCache().get(fotagModel.getActiveFotags().get(position).getId());
        imageView.setImageBitmap(bp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotagModel.updateAllViews();
                finish();
            }
        });

        ratingBar.setRating((float) fotagModel.getActiveFotags().get(position).getRating());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int newRate = (int) v;
                try{
                    fotagModel.setActiveFotagRating(position, newRate);
                }catch (Exception e){
                    Log.d("Popup", e.getMessage());
                }

            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    fotagModel.clearActiveFotagRating(position);
                    if (fotagModel.getFilterRating() == 0){
                        ratingBar.setRating(0);
                    }else{
                        finish();
                    }
                }catch (Exception e){
                    Log.d("Popup", e.getMessage());
                    finish();
                }
            }
        });
    }
}
