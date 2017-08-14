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

            }

            @Override
            public void onLivesChanged(int lives) {
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

        //Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/karate.ttf");

        /*
            * Задать шрифт
         */

        /*TextView tvScore = findViewById(R.id.score_name);
        TextView tvLives = findViewById(R.id.live_name);

        ((TextView) findViewById(R.id.actionbar_title)).setTypeface(tf);
        tvScore.setTypeface(tf);
        tvLives.setTypeface(tf);
        ((TextView) findViewById(R.id.title_name)).setTypeface(tf);

        final MainCanvas canvas = findViewById(R.id.canvas);

        tvScore.setText("Scores: 0");

        Intent iMsg = getIntent();
        if(iMsg != null) {
            canvas.setHigh(iMsg.getIntExtra(INTENT_MSG_DIFFICULTY, 0));
        }
        canvas.setLives(3);
        tvLives.setText("Lives: " + canvas.getLives());
        canvas.setDataListener(new DataListener() {
            @Override
            public void onScoresChanged(int scores) {
                ((TextView) findViewById(R.id.score_name)).setText("Scores:" + scores);
            }

            @Override
            public void onLivesChanged(int lives) {
                ((TextView) findViewById(R.id.live_name)).setText("Lives:" + lives);
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
*/
    }

    @Override
    public void onBackPressed()
    {
        MainCanvas canvas = findViewById(R.id.canvas);
        canvas.stop();
        super.onBackPressed();
    }
}
