package com.example.cardgame.auxiliaryClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitForMove {

    TextView textView;
    ImageView imageView;

    public WaitForMove(TextView textView, ImageView imageView) {
        this.textView = textView;
        this.imageView = imageView;
    }

    public void show() {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }
}
