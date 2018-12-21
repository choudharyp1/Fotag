package com.example.p2choudh.fotagp2choudh.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.p2choudh.fotagp2choudh.R;

public class FotagView extends LinearLayout {

    public FotagView(Context context) {
        super(context);
    }

    public FotagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FotagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageView(Bitmap bm){
        ImageView imageView = (ImageView) findViewById(R.id.fotag_img);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setImageBitmap(bm);
    }
}
