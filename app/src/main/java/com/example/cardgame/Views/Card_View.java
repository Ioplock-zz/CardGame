package com.example.cardgame.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Card_View extends View {

    Paint second_color = new Paint();
    Paint Color_e = new Paint();
    float viewWidth;
    float viewHeight;
    RectF Background_f;
    RectF Background_b;
    RectF Forceground;
    RectF Background_custom;
    String number = "9";
    RectF red_rect, blue_rect, yellow_rect, green_rect;

    public Card_View(Context context) {
        super(context);
        init(null);
    }

    public Card_View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Card_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public Card_View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(@Nullable AttributeSet attrs) {
    }

    public void initPaints() {
        second_color.setStyle(Paint.Style.FILL);
        second_color.setColor(Color.WHITE);
        second_color.setAntiAlias(true);
        second_color.setTextSize(viewHeight / 2 / 2 / 2);
        second_color.setTextAlign(Paint.Align.CENTER);
        Color_e.setStyle(Paint.Style.FILL);
        Color_e.setColor(Color.WHITE);
        Color_e.setAntiAlias(true);
        Color_e.setTextSize(viewHeight / 2 / 2);
        Color_e.setTextAlign(Paint.Align.CENTER);
    }

    public void initShapes() {
        int offset = 30;
        Background_f = new RectF(
                offset, // left
                offset, // top
                viewWidth - offset, // right
                viewHeight - offset // bottom
        );
        Background_custom = new RectF(
                offset - 10, // left
                offset - 10, // top
                viewWidth - offset + 10, // right
                viewHeight - offset + 10 // bottom
        );
        Background_b = new RectF(
                0, // left
                0, // top
                viewWidth, // right
                viewHeight // bottom
        );
        float viewHeight2 = (float) (((viewHeight / 2) / 2) / 1.5);
        float viewWidth2 = ((((viewHeight / 2) / 2) / 2 ) / 2);
        Forceground = new RectF(
                0 - viewWidth2, // left
                (viewHeight / 2)  - viewHeight2, // top
                viewWidth + viewWidth2, // right
                (viewHeight / 2) + viewHeight2 // bottom
        );
        red_rect = new RectF(
                offset, // left
                offset, // top
                viewWidth  /2, // right
                viewHeight / 2 // bottom
        );
        blue_rect = new RectF(
                viewWidth / 2, // left
                offset, // top
                viewWidth - offset, // right
                viewHeight / 2 // bottom
        );
        yellow_rect = new RectF(
                offset, // left
                viewHeight /2, // top
                viewWidth /2, // right
                viewHeight - offset // bottom
        );
        green_rect = new RectF(
                viewWidth / 2, // left
                viewHeight /2, // top
                viewWidth - offset, // right
                viewHeight - offset // bottom
        );
    }

    public Paint fastPaint(String color){
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setTextSize(viewHeight / 2 / 2);
        p.setTextAlign(Paint.Align.CENTER);
        switch (color) {
            case "blue": p.setColor(Color.BLUE); break;
            case "red": p.setColor(Color.RED); break;
            case "yellow": p.setColor(Color.YELLOW); break;
            case "green": p.setColor(Color.GREEN); break;
            default: p.setColor(Color.WHITE);
        }
        return p;
    }

    public void changeColor(String color) {
        switch (color) {
            case "blue": Color_e.setColor(Color.BLUE); break;
            case "red": Color_e.setColor(Color.RED); break;
            case "yellow": Color_e.setColor(Color.YELLOW); break;
            case "green": Color_e.setColor(Color.GREEN); break;
            default: Color_e.setColor(Color.WHITE);
        }
    }

    public void changeSecondColor(boolean bool) {
        if(bool) second_color.setColor(Color.BLACK); else second_color.setColor(Color.WHITE);
    }

    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        viewWidth = xNew;
        viewHeight = yNew;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initShapes();
        initPaints();
        canvas.drawRoundRect(Background_b, 20 , 20, second_color);
        Paint second_color_stroke = new Paint(second_color);
        second_color_stroke.setStyle(Paint.Style.STROKE);
        second_color_stroke.setStrokeWidth(20);
        if (Color_e.getColor() == Color.WHITE) {
            canvas.drawRect(red_rect, fastPaint("red"));
            canvas.drawRect(blue_rect, fastPaint("blue"));
            canvas.drawRect(yellow_rect, fastPaint("yellow"));
            canvas.drawRect(green_rect, fastPaint("green"));
            canvas.drawRoundRect(Background_custom,25, 25, second_color_stroke);
        } else canvas.drawRoundRect(Background_f,25, 25, Color_e);
        float rotate_angle = 40;
        canvas.rotate(-rotate_angle, viewWidth / 2, viewHeight / 2);
        canvas.drawOval(Forceground, second_color);
        canvas.rotate(rotate_angle, viewWidth / 2, viewHeight / 2);
        canvas.drawText(number, viewWidth / 2, viewHeight / 2 + viewHeight / 2 / 2 / 3, Color_e);
        canvas.drawText(number,  viewWidth /2 - viewWidth /2 /2 - viewWidth /2 /2 /2, viewHeight /2 - viewHeight /2 /2 - viewHeight /2 / 2 / 2, second_color);
        canvas.drawText(number,  viewWidth /2 + viewWidth /2 /2 + viewWidth /2 /2 /2, viewHeight /2 + viewHeight /2 /2 + viewHeight /2 / 2 / 2 + viewHeight /2 / 2 / 2 / 2 + viewHeight /2 /2 /2 /2 /2, second_color);
    }
}
