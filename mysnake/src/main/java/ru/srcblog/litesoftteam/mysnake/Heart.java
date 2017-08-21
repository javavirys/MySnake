/*
 Copyright 2017 javavirys

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package ru.srcblog.litesoftteam.mysnake;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

/*
 * Created by javavirys on 24.06.2017.
 */

public class Heart {

    private boolean flagGen;

    private int x;
    private int y;

    int xBlock;
    int yBlock;

    int wBlock;
    int hBlock;

    int bWCount;
    int bHCount;

    Paint paint;

    boolean isGraphics;
    Bitmap bmp, dstBmp;

    boolean animate;
    int offsetX = 0;
    int offsetY = 0;

    long curTime; // нужно для сравниения по времение

    public Heart(int offsetX,int offsetY,int wBlock,int hBlock,int blockWCount,int blockHCount)
    {
        this.x = offsetX;
        this.y = offsetY;
        this.wBlock = wBlock;
        this.hBlock = hBlock;
        this.bWCount = blockWCount;
        this.bHCount = blockHCount;

        flagGen = false;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff000000);
    }

    public Heart(int offsetX, int offsetY, int wBlock, int hBlock, int blockWCount, int blockHCount, Bitmap b)
    {
        this(offsetX,offsetY,wBlock,hBlock,blockWCount,blockHCount);
        bmp = b;
        dstBmp = bmp;
    }


    public void generate() {
        Random rnd = new Random();
        xBlock = Math.abs(rnd.nextInt() % bWCount);
        yBlock = Math.abs(rnd.nextInt() % bHCount);
        flagGen = true;
    }

    public boolean isGeneratedCoord() {
        return flagGen;
    }

    public void generateCoord()
    {
        flagGen = false;
    }

    public void setGraphics(boolean flag)
    {
        isGraphics = flag;
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

    public void animate()
    {
        if(System.currentTimeMillis() - curTime < 1000L)
            return;
        else
            curTime = System.currentTimeMillis();

        Matrix m = new Matrix();
        if(animate) {
            offsetX = 4;
            offsetY = 4;
            m.postScale(0.75f, 0.75f);
            animate = false;
        }
        else {
            offsetX = 0;
            offsetY = 0;
            animate = true;
        }
        dstBmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),m,true);
    }

    public void draw(Canvas canvas)
    {
        if(flagGen)
        {
            // рисуем
            //canvas.drawRect(x,y,x + wBlock,y + hBlock,paint);
            if(bmp != null && isGraphics)
                canvas.drawBitmap(dstBmp,offsetX + x + xBlock * wBlock,
                        offsetY + y + yBlock * hBlock,null);
            else
                canvas.drawRect(x + xBlock * wBlock,y + yBlock * hBlock,x + (xBlock*wBlock) + wBlock,y + (yBlock * hBlock) + hBlock, paint);
        //canvas.drawCircle(50,50,20,paint);
        }
    }

}
