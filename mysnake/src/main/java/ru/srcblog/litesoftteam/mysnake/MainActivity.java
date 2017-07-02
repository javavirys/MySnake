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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.srcblog.litesoftteam.mysnake.menu.ScoresActivity;

public class MainActivity extends Activity {

    public static String INTENT_MSG_DIFFICULTY = "Difficulty";

    MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;
        setContentView(R.layout.activity_main);

        final MainCanvas canvas = findViewById(R.id.canvas);

        Intent iMsg = getIntent();
        if(iMsg != null) {
            canvas.setHigh(iMsg.getIntExtra(INTENT_MSG_DIFFICULTY, 0));
        }
        canvas.setLives(3);
        canvas.setDataListener(new DataListener() {
            @Override
            public void onScoresChanged(int scores) {
                ((TextView) findViewById(R.id.score_name)).setText("Score:" + scores);
            }

            @Override
            public void onLivesChanged(int lives) {
                ((TextView) findViewById(R.id.live_name)).setText("Live:" + lives);
                if(lives <= 0) {
                    canvas.stop();

                    final Dialog d = new Dialog(main);
                    d.setContentView(R.layout.layout_write_score_dialog);
                    d.setTitle("New score");
                    d.setCancelable(false);
                    d.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getApplicationContext(),ScoresActivity.class);
                            EditText edit = d.findViewById(R.id.edit_name);
                            i.putExtra("score_name",edit.getText().toString());
                            i.putExtra("score_value",canvas.getScore());
                            startActivity(i);
                            finish();
                        }
                    });


                    d.show();
                }
            }
        });

        ((TextView) findViewById(R.id.live_name)).setText("Live:" + canvas.getLives());
        ((TextView) findViewById(R.id.score_name)).setText("Live:" + canvas.getScore());


        findViewById(R.id.button_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCanvas canvas = findViewById(R.id.canvas);

                int move = canvas.snake.getMove();

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
                canvas.snake.setMove(move);
            }
        });

        findViewById(R.id.button_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainCanvas canvas = findViewById(R.id.canvas);

                int move = canvas.snake.getMove();

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
                canvas.snake.setMove(move);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        MainCanvas canvas = findViewById(R.id.canvas);
        canvas.stop();
        super.onBackPressed();
    }
}
