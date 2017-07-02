package ru.srcblog.litesoftteam.mysnake;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by javavirys on 22.06.2017.
 */

public class Control {

    Paint paint;

    int x;
    int y;
    int r;

    public Control(int x, int y, int r)
    {
        this.x = x;
        this.y = y;
        this.r = r;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x45000000);

    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x,y,r,paint);
        canvas.drawCircle(x,y,r / 2,paint);
    }


}
