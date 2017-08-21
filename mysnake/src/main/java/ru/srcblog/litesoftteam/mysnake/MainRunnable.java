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

import android.util.Log;

import java.util.concurrent.TimeUnit;

import ru.srcblog.litesoftteam.mysnake.menu.Arrow;

/**
 * Created by javavirys on 21.06.2017.
 */

public class MainRunnable implements Runnable {

    private MainCanvas main;

    private boolean runnableFlag;

    private boolean gameOver;

    public boolean isTimerShow;

    public long timeout;
    public long curTime;

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
            if(!isTimerShow) {

                for(Arrow arrow : main.arrows)
                    arrow.setVisible(false);

                if (!main.heart.isGeneratedCoord()) {
                    main.heart.generate();
                }

                if (!gameOver)
                    main.snake.move();

            /*
                Столкновение с серцем
             */

                if (main.snake.checkCollision(main.heart)) {
                    Log.d(MainCanvas.LOG_NAME, "checkCollision");
                    main.heart.generateCoord();

                    main.snake.addPart();

                    main.post(new Runnable() {
                        @Override
                        public void run() {
                            main.score += 1 + (2 * main.getHigh());
                            if (main.dListener != null)
                                main.dListener.onScoresChanged(main.score);
                        }
                    });
                }

                if (!gameOver && (main.snake.checkCollision(main.PARTS_COUNTW, true) ||
                        main.snake.checkCollision(main.PARTS_COUNTH, false) ||
                        main.snake.checkCollision(-1, true) ||
                        main.snake.checkCollision(-1, false) ||
                        checkSelfCollision())) {
                    if (main.dListener != null)
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.dListener.onLivesChanged(--main.lives);
                            }
                        });

                    main.onSizeChanged(main.getWidth(), main.getHeight(), main.getWidth(), main.getHeight());

                    if (main.lives < 1) {
                        gameOver = true;
                    }
                }

            } else {
                Part head = main.snake.getHead();
                main.arrows[0].setX(main.getRectW() * (head.getXBlock() - 1));
                main.arrows[0].setY(main.y + main.getRectH() * (head.getYBlock() - 3));
                main.arrows[0].setTranform(Part.TRANS_ROT270);
                main.arrows[0].setVisible(true);

                main.arrows[1].setX(main.getRectW() * (head.getXBlock() - 1));
                main.arrows[1].setY(main.y + main.getRectH() * (head.getYBlock() + 2));
                main.arrows[1].setTranform(Part.TRANS_ROT90);
                main.arrows[1].setVisible(true);

                main.showingMsg = true;

                if (timeout < 4) {
                    timeout++;
                    try {
                        TimeUnit.MILLISECONDS.sleep(350);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    isTimerShow = false;
                    main.showingMsg = false;
                }

                main.msg = timeout == 4 ? "GO!":Long.toString(4 - timeout);

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
