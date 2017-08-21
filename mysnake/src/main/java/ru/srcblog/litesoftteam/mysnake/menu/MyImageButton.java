package ru.srcblog.litesoftteam.mysnake.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageButton;


/**
 * Created by javavirys on 20.08.2017.
 */

public class MyImageButton extends ImageButton {

    Paint pText;
    String text;
    int color;

    public MyImageButton(Context context) {
        super(context);
        init();
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        pText = new Paint(Paint.ANTI_ALIAS_FLAG);
        pText.setStyle(Paint.Style.FILL_AND_STROKE);
        //pText.setStrokeWidth(4);
        pText.setColor(Color.YELLOW);
        pText.setTextSize(40);
        color = 0;
    }

    public String getText()
    {
        return text;
    }

    public int getColor() {
        return color;
    }

    public Paint getPaint() {
        return pText;
    }

    public Typeface getFont()
    {
        return pText.getTypeface();
    }

    public void setText(String text)
    {
        this.text = text;
        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
        pText.setColor(color);
    }

    public void setPaint(Paint pText) {
        this.pText = pText;
    }

    public void setFont(Typeface font)
    {
        pText.setTypeface(font);
    }

    @Override
    protected void onDraw(Canvas c)
    {
        super.onDraw(c);

        if(text != null)
            c.drawText(text,(getWidth() - pText.measureText(text)) / 2,getHeight() / 2,pText);
    }

}
