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

package ru.srcblog.litesoftteam.mysnake.listeners;

import android.util.Log;
import android.view.MotionEvent;

import ru.srcblog.litesoftteam.mysnake.MainCanvas;
import ru.srcblog.litesoftteam.mysnake.Part;


/**
 * Created by javavirys on 07.07.2017.
 */

public class MotionListener{

    MainCanvas context;

    float startX;
    float startY;
    float endX;
    float endY;

    private static final int SWIPE_MIN_DISTANCE = 40;

    public MotionListener(MainCanvas c)
    {
        context = c;
    }

    public boolean onTouch(MotionEvent motionEvent) {

        int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

        if(action == MotionEvent.ACTION_DOWN)
        {
            Log.d("TEST","DOWN");
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        }

        if(action == MotionEvent.ACTION_UP)
        {
            Log.d("TEST","UP");
            endX = motionEvent.getX();
            endY = motionEvent.getY();

            Log.d("TEST","startX: " + startX);
            Log.d("TEST","endX: " + endX);

            int move = context.snake.getMove();

            if(move == Part.MOVE_UP) {
                if (endX - startX > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveRight();
                } else if (startX - endX > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveLeft();
                }
            } else if(move == Part.MOVE_DOWN)
            {
                if (endX - startX > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveLeft();
                } else if (startX - endX > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveRight();
                }
            } else if(move == Part.MOVE_RIGHT)
            {
                if (endY - startY > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveRight();
                } else if (startY - endY > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveLeft();
                }
            } else if(move == Part.MOVE_LEFT)
            {
                if (endY - startY > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveLeft();
                } else if (startY - endY > SWIPE_MIN_DISTANCE) {
                    context.snake.setMoveRight();
                }
            }
        }
        return true;
    }
}
