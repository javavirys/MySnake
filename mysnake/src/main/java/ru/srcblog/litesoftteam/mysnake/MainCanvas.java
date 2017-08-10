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
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ru.srcblog.litesoftteam.mysnake.listeners.MotionListener;

/**
 * Created by javavirys on 21.06.2017.
 */

public class MainCanvas extends View{

    public static String LOG_NAME = "MY_SNAKE";

    Bitmap bmpsBody[] = { Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
            R.drawable.snake_body_1), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_body_2), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_body_3), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_body_4), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_body_5), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_body_6), rectW,rectH,true)};

    Bitmap bmpsHead[] = { Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
            R.drawable.snake_head_1), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_head_2), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_head_3), rectW,rectH,true)};

    Bitmap bmpsTail[] = { Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
            R.drawable.snake_tail_1), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_tail_2), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_tail_4), rectW,rectH,true),
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.snake_tail_5), rectW,rectH,true)};


    DataListener dListener;

    private Paint paint;

    private Rect rect;

    int PARTS_COUNTW;
    int PARTS_COUNTH;

    static final int rectW = 25;
    static final int rectH = 25;

    Thread mThread;

    MainRunnable runnable;

    public Snake snake;

    public Heart heart;

    int score;
    int lives;

    int speed;

    MotionListener motionListener;

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

        rect = new Rect();

        motionListener = new MotionListener(this);

        runnable = new MainRunnable(this);
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

        PARTS_COUNTW = w / rectW;
        PARTS_COUNTH = h / rectH;

        snake = new Snake(getContext(),w,h,PARTS_COUNTW,PARTS_COUNTH);
        snake.setColored(true);

        Part tail = new Part(0, 0, rectW, rectH, bmpsTail,Part.MOVE_RIGHT);
        tail.setFrame(1);
        //int arr[] = {0,1,1,2,3,3};
        //tail.setFrameSequence(arr);
        snake.addPart(tail);

        int k = 0;
        for(int i = 1; i < 6; i++) {
            Part p = new Part(i, 0, rectW, rectH, bmpsBody,Part.MOVE_RIGHT);
            p.setFrame(Math.abs(i % 6));
            snake.addPart(p);
            k = i;
        }


        // Head
        Part head = new Part(++k,0,rectW,rectH,
                bmpsHead);
        head.setFrame(1);
        snake.addPart(head);

        heart = new Heart(rectW,rectH,PARTS_COUNTW,PARTS_COUNTH);

        Log.d(LOG_NAME,"onSizeChanged");
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

        //canvas.drawARGB(255,100,255,255);

        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.playing_field),0f,0f,null);

        /*
            Рисуем сетку
         */

        //RectF r = new RectF(0,0,getWidth(),getHeight());

        //canvas.drawRoundRect(r,20,20,paint);

        for(int i = 0; i < PARTS_COUNTW; i++)
            for(int j = 0; j < PARTS_COUNTH;     j++){
                rect.set(i * rectW,j * rectH,rectW + (rectW * i),rectH + (rectH * j));
                canvas.drawRect(rect, paint);
            }

        /*
            Рисуем змейку
         */
        snake.draw(canvas);


        /*
            Рисуем сердечко
         */
        heart.draw(canvas);
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
