package ru.srcblog.litesoftteam.mysnake;

import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by javavirys on 21.06.2017.
 */

public class MainRunnable implements Runnable {

    private MainCanvas main;

    private boolean runnableFlag;

    private boolean gameOver;

    boolean menuFlag;

    public MainRunnable(MainCanvas main)
    {
        this.main = main;
    }

    public void stop()
    {
        runnableFlag = false;
    }

    public boolean isRunning()
    {
        return runnableFlag;
    }

    @Override
    public void run() {
        runnableFlag = true;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (runnableFlag)
        {
            if(!main.heart.isGeneratedCoord()) {
                main.heart.generate();
            }

            if(!gameOver)
                main.snake.move();

            /*
                Столкновение с серцем
             */
            if(main.snake.checkCollision(main.heart))
            {
                Log.d(MainCanvas.LOG_NAME,"checkCollision");
                main.heart.generateCoord();

                main.snake.insertPart(0,new Part(main.snake.getPart(0).getXBlock(),main.snake.getPart(0).getYBlock(),
                        main.snake.getPart(0).getWBlock(),main.snake.getPart(0).getHBlock(),main.PART_COUNT));

                if(main.dListener != null)
                    main.post(new Runnable() {
                        @Override
                        public void run() {
                            main.score += 1 + (2 * main.getHigh());
                            main.dListener.onScoresChanged(main.score);
                        }
                    });
            }

            Part head = main.snake.getHead();

            if(!gameOver && (main.snake.checkCollision(main.PART_COUNT,true) ||
                    main.snake.checkCollision(main.PART_COUNT,false) ||
                    main.snake.checkCollision(-1,true) ||
                    main.snake.checkCollision(-1,false) ||
                    checkSelfCollision()))
            {
                if(main.dListener != null)
                    main.post(new Runnable() {
                        @Override
                        public void run() {
                            main.dListener.onLivesChanged(--main.lives);
                        }
                    });

                main.onSizeChanged(main.getWidth(),main.getHeight(),main.getWidth(),main.getHeight());

                if(main.lives < 1) {
                    gameOver = true;
                }
            }


            main.post(new Runnable() {
                @Override
                public void run() {
                    main.invalidate();
                }
            });

            try {
                TimeUnit.MILLISECONDS.sleep(800 / (main.getHigh() + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public boolean checkSelfCollision()
    {
        for(int i = 0; i < main.snake.getPartCount() - 1; i++) {
            Part block = main.snake.getPart(i);
            if(main.snake.checkCollision(block.getXBlock(),block.getYBlock()))
                return true;
        }
        return false;
    }

}
