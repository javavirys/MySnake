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
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ru.srcblog.litesoftteam.mysnake.listeners.MotionListener;
import ru.srcblog.litesoftteam.mysnake.menu.Arrow;
import ru.srcblog.litesoftteam.mysnake.menu.MenuActivity;

/**
 * Created by javavirys on 21.06.2017.
 */

public class MainCanvas extends View{

    public static String LOG_NAME = "MY_SNAKE";

    Bitmap bmpsBody[] = {
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_body),rectW,rectH,true)};

    Bitmap bmpsHead[] = {
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_head), rectW,rectH,true)};

    Bitmap bmpsTail[] = {
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_tail), rectW,rectH,true)};

    Bitmap bHeadBoard = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.board),204,110,true);

    Bitmap bHeadBush = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bush),119,80,true);

    Bitmap bFootFence = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.fence),53,100,true);

    Bitmap bFootBush = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.foot_bush),240,115,true);

    Bitmap apple = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
            R.drawable.apple),rectW,rectH,true);

    Bitmap bArrow = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
            R.drawable.arrow),50,50,true);

    Rect rBush[];

    DataListener dListener;

    private Paint paint;
    private Paint pText;
    private Paint pGrid;

    private Rect rect;

    int PARTS_COUNTW;
    int PARTS_COUNTH;

    static final int rectW = 25;
    static final int rectH = 25;

    Thread mThread;

    MainRunnable runnable;

    public Snake snake;

    public Heart heart;

    public Arrow arrow;

    public Arrow arrows[];

    int score;
    int lives;

    int speed;

    MotionListener motionListener;

    // Начало cтартового поля
    int x;
    public int y;

    public boolean showingMsg;
    public String msg;

    public MainCanvas(Context context) {
        super(context);
        init();
    }

    public MainCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        Log.d(LOG_NAME,"init");

        speed = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);
        paint.setColor(0xff5f8878);

        pGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
        pGrid.setStyle(Paint.Style.STROKE);
        pGrid.setStrokeWidth(1.0f);
        pGrid.setColor(0x885f8878);

        pText = new Paint(Paint.ANTI_ALIAS_FLAG);
        pText.setStyle(Paint.Style.STROKE);
        pText.setColor(Color.RED);
        Typeface tfFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/karate.ttf");
        pText.setTypeface(tfFont);
        //pText.setStrokeWidth(5.0f);

        rect = new Rect();

        motionListener = new MotionListener(this);

        runnable = new MainRunnable(this);
        runnable.isTimerShow = true;

        mThread = new Thread(runnable);
        mThread.start();

    }

    public int getRectW()
    {
        return rectW;
    }

    public int getRectH()
    {
        return rectH;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return motionListener.onTouch(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Меняем размеры всех зависящих переменных

        //w = w - w / 4;
        h = h - 240;

        x = 0;
        y = 120;

        PARTS_COUNTW = w / rectW;
        PARTS_COUNTH = h / rectH;

        // -------------------- STATIC -------------------------

        // Координаты верхних кустов
        Rect tmp[] = {
                new Rect(bHeadBoard.getWidth() - (bHeadBush.getWidth() / 2),0,0,0),
                new Rect(bHeadBoard.getWidth() - (bHeadBush.getWidth() / 2), bHeadBush.getHeight() / 2,0,0),
                new Rect(0,0,0,0),
                new Rect(bHeadBoard.getWidth() / 2,-10,0,0),
                new Rect(getWidth() / 2,0,0,0),
                new Rect(getWidth() / 2 + getWidth() / 4,0,0,0),
                new Rect(getWidth() / 2 + (getWidth() / 4 * 2),0,0,0),
                new Rect(getWidth() / 2 + (getWidth() / 6),bHeadBush.getHeight() / 2,0,0),
                new Rect(getWidth() - bHeadBush.getWidth() + 10,bHeadBush.getHeight() / 2,0,0)
        };

        rBush = tmp;

        arrow = new Arrow(bArrow);

        arrows = new Arrow[2];

        for (int i = 0; i < arrows.length; i ++) {
            arrows[i] = new Arrow(bArrow);
            arrows[i].setVisible(false);
        }

        // -------------------- GAME -------------------------
        snake = new Snake(getContext(),x,y,w,h,PARTS_COUNTW,PARTS_COUNTH);
        snake.setColored(true);

        int startX = 0;
        int startY = PARTS_COUNTH / 2;

        Part tail = new Part(startX, startY, rectW, rectH, bmpsTail,Part.MOVE_RIGHT);
        tail.setFrame(0);
        snake.addPart(tail);

        int k = 0;
        for(int i = 1; i < 3; i++) {
            Part p = new Part(i, startY, rectW, rectH, bmpsBody,Part.MOVE_RIGHT);
            p.setFrame(0);
            snake.addPart(p);
            k = i;
        }


        // Head
        Part head = new Part(++k,startY,rectW,rectH,
                bmpsHead);
        head.setFrame(0);
        snake.addPart(head);

        heart = new Heart(x,y,rectW,rectH,PARTS_COUNTW,PARTS_COUNTH,apple);
        heart.setGraphics(true);

        //Log.d(LOG_NAME,"onSizeChanged");
    }


    private int measureHeight(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Размер по умолчанию, если ограничения не были установлены.
        int result = 250;
        if (specMode == MeasureSpec.AT_MOST) {
            // Рассчитайте идеальный размер вашего
            // элемента в рамках максимальных значений.
            // Если ваш элемент заполняет все доступное
            // пространство, верните внешнюю границу.
        } else if (specMode == MeasureSpec.EXACTLY) {
            // Если ваш элемент может поместиться внутри этих границ, верните это значение.
            result = specSize;
        }
        return result;
    }

    private int measureWidth(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Размер по умолчанию, если ограничения не были установлены.
        int result = 250;
        if (specMode == MeasureSpec.AT_MOST) {
            // Рассчитайте идеальный размер вашего
            // элемента в рамках максимальных значений.
            // Если ваш элемент заполняет все доступное
            // пространство, верните внешнюю границу.
            //result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // Если ваш элемент может поместиться внутри этих границ, верните это значение.
            result = specSize;
        }
        return result;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum

        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
            Рисуем фон
         */

        MenuActivity.fillBackground(getContext(),this,getWidth(),getHeight());

        /*
            Рисуем шапку
         */

        canvas.drawBitmap(bHeadBoard,0,0,null);

        // Рисуем на доске счет и жизни

        pText.setTextSize(20);

        String str = "Lives: " + lives;

        int startTextX = (int) ((bHeadBoard.getWidth() - pText.measureText(str)) / 2);

        int startTextY = (bHeadBoard.getHeight() / 2) + (int)pText.getTextSize();

        canvas.drawText(str, startTextX,startTextY,pText);

        str = "Score: " + score;

        startTextY = bHeadBoard.getHeight() / 4 + (bHeadBoard.getHeight() / 2) + (int)pText.getTextSize();

        canvas.drawText(str, startTextX, startTextY, pText);

        // Рисуем кустики

        for(Rect r : rBush)
            canvas.drawBitmap(bHeadBush,r.left,r.top,null);

        // Рисуем низ

        for(int i = 0; i < getWidth() / (bFootBush.getWidth() / 2); i++)
            canvas.drawBitmap(bFootBush,i * (bFootBush.getWidth() / 2) - (bFootBush.getWidth() / 4),
                    getHeight() - bFootBush.getHeight(),null); // кусты

        for(int i = 0; i < (getWidth() / bFootFence.getWidth()) +1; i ++)
            canvas.drawBitmap(bFootFence,i * bFootFence.getWidth(),getHeight() - bFootFence.getHeight(),null); // Забор

        /*
            Рисуем сетку
         */

        for(int i = 0; i < PARTS_COUNTW; i++)
            for(int j = 0; j < PARTS_COUNTH; j++){
                rect.set(x + i * rectW,y + j * rectH,x + rectW + (rectW * i),y + rectH + (rectH * j));
                canvas.drawRect(rect, pGrid);
            }

        /*
            Рисуем змейку
         */
        snake.draw(canvas);

        /*
            Рисуем сердечко
         */
        heart.animate();
        heart.draw(canvas);

        arrow.onDraw(canvas);

        for(int i = 0; i < arrows.length; i ++)
            arrows[i].onDraw(canvas);

        if(showingMsg && msg != null)
        {
            Paint p = new Paint(pText);
            p.setTextSize(40);
            canvas.drawText(msg,(getWidth() - pText.measureText(msg)) / 2, getHeight() / 2,p);
        }

    }

    public void stop()
    {
        runnable.stop();
    }

    public void setDataListener(DataListener listener)
    {
        dListener = listener;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int value)
    {
        lives = value;
    }

    public int getScore()
    {
        return score;
    }

    public int getHigh()
    {
        return speed;
    }

    public void setHigh(int speed)
    {
        this.speed = speed;
    }

}

interface DataListener
{
    void onScoresChanged(int scores);
    void onLivesChanged(int lives);
}
