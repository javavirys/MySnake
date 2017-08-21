package ru.srcblog.litesoftteam.mysnake.menu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import ru.srcblog.litesoftteam.mysnake.Part;

/**
 * Created by javavirys on 21.08.2017.
 */

public class Arrow {

    Bitmap bmp;
    Bitmap dstBmp;
    int x,y;
    boolean isVisible;

    public Arrow(Bitmap bitmap)
    {
        bmp = bitmap;
        dstBmp = Bitmap.createBitmap(bmp);
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setTranform(int transform)
    {
        Matrix matrix = new Matrix();
        switch (transform)
        {
            case Part.TRANS_NONE:
                matrix.postRotate(0);
                break;
            case Part.TRANS_ROT90:
                matrix.postRotate(90);
                break;
            case Part.TRANS_ROT180:
                matrix.postRotate(180);
                break;
            case Part.TRANS_ROT270:
                matrix.postRotate(270);
                break;
            case Part.TRANS_MIRROR:
                matrix.postScale(-1,-1);
                break;
            case Part.TRANS_MIRROR_ROT90:
            case Part.TRANS_MIRROR_ROT180:
            case Part.TRANS_MIRROR_ROT270:
                matrix.postScale(-1,-1);
                break;
        }
        dstBmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void onDraw(Canvas c)
    {
        if(isVisible)
           c.drawBitmap(dstBmp,x,y,null);
    }

}
