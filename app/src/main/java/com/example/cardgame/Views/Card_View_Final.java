package com.example.cardgame.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.R;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

public class Card_View_Final extends View {

    Context context;
    boolean isInited;
    // Init background and icons
    Drawable background = getResources().getDrawable(R.drawable.card_red, null);
    VectorMasterDrawable skip;
    VectorMasterDrawable reverse;
    VectorMasterDrawable flip;

    // Colors constants
    private final int BLUE_COLOR = getResources().getColor(R.color.blue), RED_COLOR = getResources().getColor(R.color.red), GREEN_COLOR = getResources().getColor(R.color.green), YELLOW_COLOR = getResources().getColor(R.color.yellow);

    // Screen size
    int height, width;

    // Card data
    Paint text_color = new Paint(); // Цвет которым будут нарисованы значки и цифры на картах
    public Card nowCard = new Card("wild", "default", 1, "wild", "default", 1, 999);

    public Card_View_Final(Context context) {
        super(context);
        this.context = context;
    }

    public Card_View_Final(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        height = h;
        width = w;
    }

    // Задаёт значение базовым переменным
    private void init() {
        if(!isInited) {
            initPaints();
            Matrix scale = new Matrix();
            scale.setScale(height/1197.5f, height/1197.5f);
            Matrix offset = new Matrix();
            offset.setTranslate(width/6f, height/3.2f);

            skip = new VectorMasterDrawable(context, R.drawable.skip);
            skip.setHotspotBounds(0, 0, width, height);
            skip.getFullPath().transform(scale);
            skip.getFullPath().transform(offset);
            flip = new VectorMasterDrawable(context, R.drawable.flip);
            flip.setHotspotBounds(0, 0, width, height);
            flip.getFullPath().transform(scale);
            flip.getFullPath().transform(offset);
            reverse = new VectorMasterDrawable(context, R.drawable.reverse);
            reverse.setHotspotBounds(0, 0, width, height);
            reverse.getFullPath().transform(scale);
            reverse.getFullPath().transform(offset);
            isInited = true;
        }
    }

    // Задаёт значения кистям
    private void initPaints(){
        text_color.setStyle(Paint.Style.FILL);
        text_color.setAntiAlias(true);
        text_color.setColor(RED_COLOR);
        text_color.setTextSize(height / 2f / 2f);
        text_color.setTextAlign(Paint.Align.CENTER);
    }

    // Возможность поменять нарисованную на View карту
    public void changeCard(Card card) {
        initPaints();
        switch (card.getColorNow()) {
            case "blue":
                background = getResources().getDrawable(R.drawable.card_blue, null);
            text_color.setColor(BLUE_COLOR);
            break;
            case "red":
                background = getResources().getDrawable(R.drawable.card_red, null);
            text_color.setColor(RED_COLOR);
            break;
            case "green":
                background = getResources().getDrawable(R.drawable.card_green, null);
                text_color.setColor(GREEN_COLOR);
                break;
            case "yellow":
                background = getResources().getDrawable(R.drawable.card_yellow, null);
                text_color.setColor(YELLOW_COLOR);
                break;
            case "wild":
                background = getResources().getDrawable(R.drawable.card_wild, null);
                text_color.setColor(YELLOW_COLOR);
                break;
            case "end":
                background = getResources().getDrawable(R.drawable.card_black, null);
                text_color.setColor(Color.BLACK);
        }
        nowCard = card;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        if(Card.flip) canvas.drawColor(Color.parseColor("#666666")); else canvas.drawColor(Color.WHITE);
        background.setBounds(0, 0, width, height);
        background.draw(canvas);
        switch (nowCard.getTypeNow()) { // Не обращайте внимания что значки рисуються не на своих местах, так же для wild карты пока нет фона
            case "flip": canvas.drawPath(flip.getFullPath(), text_color);break;
            case "skip": canvas.drawPath(skip.getFullPath(), text_color); break;
            case "reverse": canvas.drawPath(reverse.getFullPath(), text_color); break;
            case "end": canvas.drawText("F", width / 2f,height / 2f + height / 16f + height / 32f, text_color); break;
            default: canvas.drawText(nowCard.getNumberString(), width / 2f,height / 2f + height / 16f + height / 32f, text_color);
        }
    }
}
