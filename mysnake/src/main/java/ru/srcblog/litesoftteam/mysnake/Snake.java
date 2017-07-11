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

import java.util.ArrayList;

/**
 * Created by javavirys on 21.06.2017.
 */

public class Snake {

    private ArrayList<Part> parts;

    private int move;

    Paint paint;

    int w;
    int h;
    int countBlocks;

    boolean isVisible;

    public Snake(int w,int h,int countBlock)
    {
        this.w = w;
        this.h = h;
        this.countBlocks = countBlock;

        parts = new ArrayList<>();
        move = Part.MOVE_RIGHT;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff000000);
        isVisible = true;
    }

    public void addPart(Part p)
    {
        parts.add(p);
    }

    public void removePart(int index)
    {
        parts.remove(index);
    }

    public void insertPart(int index,Part p)
    {
        parts.add(index,p);
    }

    public Part getPart(int index)
    {
        return parts.get(index);
    }

    public int getPartCount()
    {
        return parts.size();
    }

    public Part getHead()
    {
        return getPart(getPartCount() - 1);
    }

    public int getMove()
    {
        return move;
    }

    public void setMove(int direction)
    {
        if(direction == move || (direction == Part.MOVE_LEFT && move == Part.MOVE_RIGHT) ||
                (direction == Part.MOVE_RIGHT && move == Part.MOVE_LEFT) )
            return;

        move = direction;
    }

    public void setMoveLeft()
    {
        switch (move){
            case Part.MOVE_RIGHT:
                move =  Part.MOVE_UP;
                break;
            case Part.MOVE_DOWN:
                move = Part.MOVE_RIGHT;
                break;
            case Part.MOVE_LEFT:
                move = Part.MOVE_DOWN;
                break;
            case Part.MOVE_UP:
                move = Part.MOVE_LEFT;
                break;
        }
    }

    public void setMoveRight()
    {
        switch (move){
            case Part.MOVE_RIGHT:
                move =  Part.MOVE_DOWN;
                break;
            case Part.MOVE_DOWN:
                move = Part.MOVE_LEFT;
                break;
            case Part.MOVE_LEFT:
                move = Part.MOVE_UP;
                break;
            case Part.MOVE_UP:
                move = Part.MOVE_RIGHT;
                break;
        }
    }

    public int getBlockXIndex(int x)
    {

        return (x * countBlocks) / w;
    }

    public int getBlockYIndex(int y)
    {

        return (y * countBlocks) / h;
    }


    public boolean checkCollision(int xyBlock,boolean isXCoord)
    {
        Part pHead = getHead();
        int coord;
        if(isXCoord)
            coord = pHead.getXBlock();
        else
            coord = pHead.getYBlock();

        if(coord == xyBlock)
            return true;
        return false;
    }

    public boolean checkCollision(int xBlock,int yBlock)
    {
        Part pHead = getHead();
        if(pHead.getXBlock() == xBlock && pHead.getYBlock() == yBlock) {
            return true;
        }
        return false;
    }

    public boolean checkCollision(Heart h)
    {
        Part tail = parts.get(0);
        if(h.getXBlock() == tail.getXBlock() && h.getYBlock() == tail.getYBlock())
        {
            return true;
        }
        return false;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }

    /*
        Движение змейки
        нужно розобраться, как поворачивать
     */
    public void move()
    {
        Part head = parts.get(parts.size() - 1);

        Part oldPart = null;

        if(move == Part.MOVE_RIGHT)
        {
            oldPart = new Part(head);
            head.xBlock ++;
        } else if(move == Part.MOVE_DOWN)
        {
            oldPart = new Part(head);
            head.yBlock ++;
        } else if(move == Part.MOVE_LEFT)
        {
            oldPart = new Part(head);
            head.xBlock --;
        } else if(move == Part.MOVE_UP)
        {
            oldPart = new Part(head);
            head.yBlock --;
        }

        if(oldPart == null)
            return;

        int xBlock;
        int yBlock;


        for(int i = parts.size() - 2; i > -1; i--) {
            Part p = parts.get(i);

            xBlock = p.xBlock;
            yBlock = p.yBlock;


            p.xBlock = oldPart.xBlock;
            p.yBlock = oldPart.yBlock;

            oldPart = new Part();
            oldPart.xBlock = xBlock;
            oldPart.yBlock = yBlock;
        }
    }

    public void draw(Canvas canvas)
    {
        for(int i = 0; i < getPartCount(); i++) {
            Part p = getPart(i);
            canvas.drawRect(p.xBlock * p.wBlock,p.yBlock * p.hBlock,p.xBlock * p.wBlock + p.wBlock,p.yBlock * p.hBlock + p.hBlock,
                    paint);
        }

    }

}
