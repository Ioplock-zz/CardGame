package com.example.cardgame.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

import com.example.cardgame.auxiliaryClasses.Card;
import com.example.cardgame.Card_Vars.Colors;
import com.example.cardgame.R;
import com.example.cardgame.Card_Vars.Type;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;

public class Card_View_Final extends View {

    Context context;

    // Init background and icons
    Drawable background = getResources().getDrawable(R.drawable.card_red, null);
    Path skip;
    Path change_direction;
    Path flip;

    // Colors constants
    private final int BLUE_COLOR = getResources().getColor(R.color.blue), RED_COLOR = getResources().getColor(R.color.red), GREEN_COLOR = getResources().getColor(R.color.green), YELLOW_COLOR = getResources().getColor(R.color.yellow);

    // Screen size
    int height, width;

    // Card data
    Paint text_color = new Paint();
    public Card nowCard = new Card("red", "default", 1, "red", "default", 1);

    public Card_View_Final(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Card_View_Final(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        height = h;
        width = w;
    }

    private void init() {
        initPaints();
        skip = new VectorMasterDrawable(context, R.drawable.skip).getFullPath();
        change_direction = new VectorMasterDrawable(context, R.drawable.flip).getFullPath();
        flip = new VectorMasterDrawable(context, R.drawable.change_direction).getFullPath();
    }

    private void initPaints(){
        text_color.setStyle(Paint.Style.FILL);
        text_color.setAntiAlias(true);
        text_color.setColor(RED_COLOR);
        text_color.setTextSize(height / 2f / 2f);
        text_color.setTextAlign(Paint.Align.CENTER);
    }

    public void changeCard(Card card) {
        initPaints();
        switch (card.getColorNow()) {
            case "blue": background = getResources().getDrawable(R.drawable.card_blue, null); text_color.setColor(BLUE_COLOR); break;
            case "red": background = getResources().getDrawable(R.drawable.card_red, null); text_color.setColor(RED_COLOR); break;
            case "green": background = getResources().getDrawable(R.drawable.card_green, null); text_color.setColor(GREEN_COLOR); break;
            case "yellow": background = getResources().getDrawable(R.drawable.card_yellow, null); text_color.setColor(YELLOW_COLOR); break;
            case "wild": background = getResources().getDrawable(R.drawable.ic_launcher_background, null);
        }
        nowCard = card;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) { //TODO: Здесь не рисуется цифра при первой отрисовке
        if(Card.flip) canvas.drawColor(Color.parseColor("#666666")); else canvas.drawColor(Color.WHITE);
        background.setBounds(0, 0, width, height);
        background.draw(canvas);
        switch (nowCard.getTypeNow()) {
            case "flip": canvas.drawPath(flip, text_color); break;
            case "skip": canvas.drawPath(skip, text_color); break;
            case "reverse": canvas.drawPath(change_direction, text_color); break;
            default: canvas.drawText(nowCard.getNumberString(), width / 2f,height / 2f + height / 16f + height / 32f, text_color);
        }
    }
}
