/**
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

    int bWCount;
    int bHCount;

    Paint paint;


    public Heart(int wBlock,int hBlock,int blockWCount,int blockHCount)
    {
        this.wBlock = wBlock;
        this.hBlock = hBlock;
        this.bWCount = blockWCount;
        this.bHCount = blockHCount;

        flagGen = false;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff000000);
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
