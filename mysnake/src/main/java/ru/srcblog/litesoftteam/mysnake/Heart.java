package ru.srcblog.litesoftteam.mysnake;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by javavirys on 24.06.2017.
 */

public class Heart {

    private boolean flagGen;

    int x;
    int y;

    int xBlock;
    int yBlock;

    int wBlock;
    int hBlock;

    int bCount;

    Paint paint;


    public Heart(int wBlock,int hBlock,int blockCount)
    {
        this.wBlock = wBlock;
        this.hBlock = hBlock;
        this.bCount = blockCount;

        flagGen = false;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff000000);
    }


    public void generate() {
        Random rnd = new Random();
        xBlock = Math.abs(rnd.nextInt() % bCount);
        yBlock = Math.abs(rnd.nextInt() % bCount);
        flagGen = true;
    }

    public boolean isGeneratedCoord() {
        return flagGen;
    }

    public void generateCoord()
    {
        flagGen = false;
    }

    public int getX() {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getXBlock()
    {
        return xBlock;
    }

    public int getYBlock()
    {
        return yBlock;
    }

    public void draw(Canvas canvas)
    {
        if(flagGen)
        {
            // рисуем
            //canvas.drawRect(x,y,x + wBlock,y + hBlock,paint);
            canvas.drawRect(xBlock * wBlock,yBlock * hBlock, (xBlock*wBlock) + wBlock, (yBlock * hBlock) + hBlock, paint);
        //canvas.drawCircle(50,50,20,paint);
        }
    }

}
