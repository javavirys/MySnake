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

    Bitmap bHead = BitmapFactory.decodeResource(getResources(),R.drawable.head1);

    Bitmap apple = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
            R.drawable.apple),rectW,rectH,true);

    DataListener dListener;

    private Paint paint;
    private Paint pText;

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

    // Начало cтартового поля
    int x;
    int y;


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

        pText = new Paint(Paint.ANTI_ALIAS_FLAG);
        pText.setStyle(Paint.Style.STROKE);
        pText.setColor(Color.RED);
        Typeface tfFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/karate.ttf");
        pText.setTypeface(tfFont);
        //pText.setStrokeWidth(5.0f);

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

        //w = w - w / 4;
        h = h - (h / 4);

        x = 0;
        y = h / 4 + 40;

        PARTS_COUNTW = w / rectW;
        PARTS_COUNTH = h / rectH;

        // -------------------- STATIC -------------------------
        // head
        bHead = Bitmap.createScaledBitmap(bHead,w,y,true);

        // -------------------- GAME -------------------------
        snake = new Snake(getContext(),x,y,w,h,PARTS_COUNTW,PARTS_COUNTH);
        //snake.setColored(true);
        snake.setColored(true);

        int startX = 0;
        int startY = PARTS_COUNTH / 2;

        Part tail = new Part(startX, startY, rectW, rectH, bmpsTail,Part.MOVE_RIGHT);
        tail.setFrame(0);
        //int arr[] = {0,1,1,2,3,3};
        //tail.setFrameSequence(arr);
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

        /*
            Рисуем шапку
         */
        canvas.drawBitmap(bHead,0,0,null);

        float dpi = getResources().getDisplayMetrics().density;
        //Log.d(MainCanvas.LOG_NAME,"dpi: " + dpi);
        int xLivesText = 0;
        int yLivesText = 0;
        int xScoreText = 0;
        int yScoreText = 0;

        if(dpi == 3.0) { //xxhdpi
            pText.setTextSize(40);
            xLivesText = (getWidth() / 2) / 2;
            yLivesText = y - 120;
            xScoreText = xLivesText;
            yScoreText = y - 60;
        }else if(dpi == 2.0){ // xhdpi
            pText.setTextSize(50);
            xLivesText = (getWidth() / 2) / 2 - 30;
            yLivesText = y - 130;

            xScoreText = xLivesText;
            yScoreText = y - 55;
        } else if(dpi == 1.5) //hdpi
        {
            pText.setTextSize(30);
            xLivesText = (getWidth() / 2) / 2 - 30;
            yLivesText = y - 55;

            xScoreText = xLivesText;
            yScoreText = y - 20;
        } else if(dpi == 1.0) // mdpi
        {
            pText.setTextSize(40);
            xLivesText = (getWidth() / 2) / 2 - 30;
            yLivesText = y - 90;

            xScoreText = xLivesText;
            yScoreText = y - 35;
        }

        canvas.drawText("Lives: " + lives, xLivesText, yLivesText, pText);
        canvas.drawText("Score: " + score, xScoreText, yScoreText, pText);

        /*
            Рисуем сетку
         */

        for(int i = 0; i < PARTS_COUNTW; i++)
            for(int j = 0; j < PARTS_COUNTH; j++){
                rect.set(x + i * rectW,y + j * rectH,x + rectW + (rectW * i),y + rectH + (rectH * j));
                canvas.drawRect(rect, paint);
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
