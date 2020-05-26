package com.example.cardgame.auxiliaryClasses;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitForMove {

    TextView textView;
    ImageView imageView;
    Button button;
    public static boolean isEnabled;

    public WaitForMove(TextView textView, ImageView imageView, Button button) {
        this.textView = textView;
        this.imageView = imageView;
        this.button = button;
    }

    public void show() {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        isEnabled = false;
    }

    public void hide() {
        textView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        button.setEnabled(true);
        isEnabled = true;
    }
}
