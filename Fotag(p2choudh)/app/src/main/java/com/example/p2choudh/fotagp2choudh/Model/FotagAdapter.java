package com.example.p2choudh.fotagp2choudh.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;

import com.example.p2choudh.fotagp2choudh.MainActivity;
import com.example.p2choudh.fotagp2choudh.Popup;
import com.example.p2choudh.fotagp2choudh.R;
import com.example.p2choudh.fotagp2choudh.View.FotagView;

public class FotagAdapter extends BaseAdapter{
    private FotagModel fotagModel;
    private Context context;
    private LayoutInflater inflater;
    private int height;
    private int width;
    private int screenWidth;
    private int screenHeight;

    public FotagAdapter(FotagModel fotagModel, Context context) {
        this.fotagModel = fotagModel;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
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
    }

    @Override
    public int getCount() {
        return fotagModel.getActiveFotags().size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final FotagView fotagView;
        if (view == null){
            fotagView = (FotagView)inflater.inflate(R.layout.fotag_view, null);
            fotagView.setLayoutParams(new GridView.LayoutParams(width, height));
        }else{
            fotagView = (FotagView) view;
        }
        Bitmap bp = fotagModel.getCache().get(fotagModel.getActiveFotags().get(i).getId());
        fotagView.setImageView(bp);


        fotagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Popup.class);
                intent.putExtra("position", i);
                Activity ac = (Activity) context;
                ac.startActivityForResult(intent, 1);
            }
        });


        final RatingBar ratingBar = (RatingBar) fotagView.findViewById(R.id.fotag_rating_ar);
        Button resetbtn = (Button) fotagView.findViewById(R.id.fotag_reset_rate);
        resetbtn.setOnClickListener(null);
        ratingBar.setOnRatingBarChangeListener(null);
        ratingBar.setRating((int) fotagModel.getActiveFotags().get(i).getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int newRate = (int) v;
                fotagModel.setActiveFotagRating(i, newRate);
            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotagModel.clearActiveFotagRating(i);
                if (fotagModel.getFilterRating() == 0){
                    ratingBar.setRating(0);
                }
            }
        });

        return fotagView;
    }
}
