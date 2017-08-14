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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by javavirys on 21.06.2017.
 */

public class Snake {

    Context context;

    private ArrayList<Part> parts;


    Paint paint;

    int x;
    int y;
    int w;
    int h;

    int countBlocksW;
    int countBlocksH;

    boolean isVisible;

    boolean isColored;

    ArrayList<Turn> turnsList;

    public Snake(Context c,int x,int y,int w,int h,int countBlockW,int countBlockH)
    {
        context = c;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.countBlocksW = countBlockW;
        this.countBlocksH = countBlockH;
        isColored = false;

        parts = new ArrayList<>();
        turnsList = new ArrayList<>();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff000000);

        isColored = false;

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
        return getHead().getDirection();
    }

    public void setMove(int direction)
    {

        if(direction == getMove() || (direction == Part.MOVE_LEFT && getMove() == Part.MOVE_RIGHT) ||
                (direction == Part.MOVE_RIGHT && getMove() == Part.MOVE_LEFT) )
            return;
        Part head = getHead();
        head.setDirection(direction);
    }

    public void setMoveLeft()
    {
        Part p = getHead();
        int move = 0;
        switch (p.getDirection()) {
            case Part.MOVE_RIGHT:
                move = Part.MOVE_UP;
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
        p.setDirection(move);

        Turn t = new Turn();
        t.x = p.getXBlock();
        t.y = p.getYBlock();
        t.direction = p.getDirection();
        turnsList.add(t);
    }

    public void setMoveRight()
    {
        Part p = getHead();

        int move = 0;
        switch (p.getDirection()) {
            case Part.MOVE_RIGHT:
                move = Part.MOVE_DOWN;
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
        p.setDirection(move);
        Turn t = new Turn();
        t.x = p.getXBlock();
        t.y = p.getYBlock();
        t.direction = p.getDirection();
        turnsList.add(t);
    }

    public int getBlockXIndex(int x)
    {
        return (x * countBlocksW) / w;
    }

    public int getBlockYIndex(int y)
    {
        return (y * countBlocksH) / h;
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
        Part head = getHead();
        if(h.getXBlock() == head.getXBlock() && h.getYBlock() == head.getYBlock())
            return true;
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

    public void setColored(boolean colored) {
        isColored = colored;
    }

    /*
        Движение змейки
        нужно розобраться, как поворачивать
     */

    public void move()
    {
        Part head = parts.get(parts.size() - 1);

        Part oldPart = null;

        /*
            Сдвигаем голову по направлению на один куб
         */
        if(head.getDirection() == Part.MOVE_RIGHT)
        {
            oldPart = new Part(head);
            head.xBlock ++;

        } else if(head.getDirection() == Part.MOVE_DOWN)
        {
            oldPart = new Part(head);
            head.yBlock ++;
        } else if(head.getDirection() == Part.MOVE_LEFT)
        {
            oldPart = new Part(head);
            head.xBlock --;
        } else if(head.getDirection() == Part.MOVE_UP)
        {
            oldPart = new Part(head);
            head.yBlock --;
        }
        if(oldPart == null)
            return;

        // Обьявляем переменные для сохранения направления и координат квадратиков змейки
        int xBlock;
        int yBlock;
        int move;
        //Bitmap swapBmp;

        // Запускаем цикл в котором каждый квадратик принимает
        // старые координаты предыдущего квадратика
        for(int i = parts.size() - 2; i > -1; i--) {
            Part p = parts.get(i);
            // animate
            //p.nextFrame();
            // end of animate

            xBlock = p.xBlock;
            yBlock = p.yBlock;
            move = p.getDirection();

            //swapBmp = p.getSwapBmp() == null ? null : Bitmap.createBitmap(p.getSwapBmp()); // test

            p.xBlock = oldPart.xBlock;
            p.yBlock = oldPart.yBlock;
            p.setDirection(oldPart.getDirection()); //
            p.setVisible(true);

            // ***************** Поворот ***************** \\
            if(p.getDirection() != move)
            {
                //Log.d(MainCanvas.LOG_NAME,"Direction is changed");
                //Log.d(MainCanvas.LOG_NAME,"turn count:" + turnsList.size());
                for(int j = 0; j < turnsList.size(); j ++)
                {
                    Turn t = turnsList.get(j);

                    // Текущая часть попала в один из поворотов
                    if(t.x == p.getXBlock() && t.y == p.getYBlock()) {
                        //Log.d(MainCanvas.LOG_NAME,"turn: getDirection: " + p.getDirection() + " move:" + move);

                        // Если прошлый квадрат не видим, то показываем его
                        Part parent = parts.get(i + 1);
                        if (parent.getSwapBmp() != null) {
                            Turn tmpTurn = null;
                            // Если следующий поворот существует
                            if(j + 1 < turnsList.size())
                                tmpTurn = turnsList.get(j + 1);
                            // Если следующая клетка это не поворот
                            // то меняем изгиб на линию
                            if(tmpTurn == null ||
                                    !(tmpTurn.x == parent.getXBlock() &&
                                            tmpTurn.y == parent.getYBlock()))
                                parent.setSwapBmp(null);
                        }
                        // ************************************************

                        if (i > 0)
                        {
                            Bitmap tmp;

                            // TODO Подогнать под изгибы

                            tmp = BitmapFactory.decodeResource(
                                    context.getResources(),
                                    R.drawable.snake_around);

                            tmp = Bitmap.createScaledBitmap(
                                    tmp, MainCanvas.rectW, MainCanvas.rectH, true);

                            tmp = diformationBmp(tmp,move,p.getDirection());
                            p.setSwapBmp(tmp);

                        }else if(i == 0)
                            if(turnsList.size() > 0)
                                turnsList.remove(0);
                    }
                }
            }
            // ***************** Конец поворота ***************** \\

            oldPart = new Part(p);
            oldPart.xBlock = xBlock;
            oldPart.yBlock = yBlock;
            oldPart.setDirection(move);
            //oldPart.setSwapBmp(swapBmp);// test
        }


    }

    private Bitmap diformationBmp(Bitmap tmp,int oldMove,int newMove)
    {
        Matrix matrix = new Matrix();
        if(oldMove == Part.MOVE_RIGHT && newMove == Part.MOVE_UP) {
            matrix.postScale(1,-1);
        } else if(oldMove == Part.MOVE_LEFT)
        {
            if(newMove == Part.MOVE_UP)
                matrix.postScale(-1,-1);
            else
                matrix.postScale(-1,1);
        } else if(oldMove == Part.MOVE_DOWN) {
            if(newMove == Part.MOVE_LEFT)
                matrix.postRotate(90);
            else
            {
                matrix.postRotate(270);
                matrix.postScale(1,-1);
            }
        } else if(oldMove == Part.MOVE_UP)
        {
            //Log.d(MainCanvas.LOG_NAME,"old Move: UP new Move: " + newMove);
            if(newMove == Part.MOVE_LEFT)
                matrix.postScale(-1,1);
            else
                matrix.postScale(-1, -1);

            matrix.postRotate(90);
        }

        return Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(),
                tmp.getHeight(), matrix,true);
    }

    public void draw(Canvas canvas)
    {
        for(int i = 0; i < getPartCount(); i++) {
            Part p = getPart(i);
            if(!p.isVisible())
                continue;

            if(isColored && p.getBmp() != null)
            {
                canvas.drawBitmap(p.getBmp(),x + p.xBlock * p.wBlock,
                        y + p.yBlock * p.hBlock,paint);
            }
            else {
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(x + p.xBlock * p.wBlock, y + p.yBlock * p.hBlock,
                        x + p.xBlock * p.wBlock + p.wBlock, y + p.yBlock * p.hBlock + p.hBlock,
                        paint);
            }
        }
    }
}

class Turn
{
    int x;
    int y;
    int direction;
}
