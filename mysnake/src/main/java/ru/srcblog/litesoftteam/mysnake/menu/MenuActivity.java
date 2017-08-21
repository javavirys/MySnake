/*
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

package ru.srcblog.litesoftteam.mysnake.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.frakbot.jumpingbeans.JumpingBeans;

import ru.srcblog.litesoftteam.mysnake.MainActivity;
import ru.srcblog.litesoftteam.mysnake.MainCanvas;
import ru.srcblog.litesoftteam.mysnake.R;

public class MenuActivity extends Activity implements Animation.AnimationListener{

    MenuActivity main;

    InterstitialAd mInterstitialAd = null;

    private AdView mAdView;

    Animation animTitle, animUpMenu, animDownMenu;

    int width;
    int height;

    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;
        setContentView(R.layout.activity_menu);

        // получили размеры экрана
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        // показать объявление
        MobileAds.initialize(this, "ca-app-pub-3444537992139784~9452329753");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3444537992139784/1929062951");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(MainCanvas.LOG_NAME,"onAdLoaded");
                if(mInterstitialAd == null && mInterstitialAd.getAdUnitId() == null)
                    return;
                mInterstitialAd.show();
            }

            /*@Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i(MainCanvas.LOG_NAME,"onAdFailed: " + errorCode);
            }*/
                                      });

        //AdRequest adRequest = new AdRequest.Builder()//.addTestDevice("8C42340767A25C05F0527E18EC82509B")
                //.build();

        //mInterstitialAd.loadAd(adRequest);

        font = Typeface.createFromAsset(getAssets(), "fonts/karate.ttf");

        fillBackground(this,findViewById(R.id.menu_container),width,height); // Заполняем фон

        fillHead(); // Заполняем вверх

        fillMenuSpace();

        fillFoot();// Заполняем низ

    }

    public static void fillBackground(Context context, View v, int width, int height)
    {

        Bitmap bmp = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bmp);

        Bitmap bPart = BitmapFactory.decodeResource(context.getResources(),R.drawable.menu_background1);
        bPart = Bitmap.createScaledBitmap(bPart,100,100,true);

        for(int i = 0; i < width / bPart.getWidth() + 1; i++)
            for(int j = 0; j < height / bPart.getHeight() + 1; j ++)
                c.drawBitmap(bPart,bPart.getWidth() * i,bPart.getHeight() * j,null);

        // Устанавливаем фон
        BitmapDrawable d = new BitmapDrawable(context.getResources(),bmp);

        //(R.id.menu_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(d);
        } else
            v.setBackgroundDrawable(d);
    }

    private void fillHead()
    {
        View v = findViewById(R.id.menu_head);
        v.setVisibility(View.INVISIBLE);
        ((TextView)v).setTypeface(font);
        animTitle = AnimationUtils.loadAnimation(this, R.anim.anim_menu_title);
        animTitle.setAnimationListener(this);
        v.startAnimation(animTitle);
    }

    private void fillMenuSpace()
    {
        View v = findViewById(R.id.menu_layout_up);
        animUpMenu = AnimationUtils.loadAnimation(this, R.anim.anim_menu_top);
        animUpMenu.setAnimationListener(this);
        v.startAnimation(animUpMenu);

        v = findViewById(R.id.menu_layout_down);
        animDownMenu = AnimationUtils.loadAnimation(this, R.anim.anim_menu_body_bottom);
        animDownMenu.setAnimationListener(this);
        v.startAnimation(animDownMenu);

    }

    private void fillFoot()
    {

    }

    private  void newGame()
    {
        final Dialog d = new Dialog(main);
        d.setContentView(R.layout.layout_start_dialog);
        d.setTitle("Play setting");

        d.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                SeekBar bar = d.findViewById(R.id.seek_high_level);

                i.putExtra(MainActivity.INTENT_MSG_DIFFICULTY, bar.getProgress());
                startActivity(i);
            }
        });

        d.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.show();
    }

    private void showScores()
    {
        Intent i = new Intent(getApplicationContext(), ScoresActivity.class);
        startActivity(i);
    }

    private void showAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle("About");
        builder.setIcon(R.mipmap.icon);
        builder.setMessage("Vendor: javavirys\nGraphics: yura301992");
        builder.setPositiveButton("GitHub", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri address = Uri.parse("https://github.com/javavirys");
                Intent openLink = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLink);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if(animation == animTitle) {
            View v = findViewById(R.id.menu_head);
            v.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation == animTitle)
        {
            View v = findViewById(R.id.menu_head);
            v.setVisibility(View.VISIBLE);

            ((TextView)v).setMovementMethod(LinkMovementMethod.getInstance());
            JumpingBeans.with((TextView)v).makeTextJump(0,  ((TextView)v).getText().length())
                    .build();
        } else if(animation == animUpMenu)
        {
            MyImageButton imgButton = findViewById(R.id.button_new_game);
            imgButton.setColor(Color.YELLOW);
            imgButton.setFont(font);
            imgButton.setText("Game");
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newGame();
                }
            });

            imgButton = findViewById(R.id.button_scores);
            imgButton.setColor(Color.GREEN);
            imgButton.setFont(font);
            imgButton.setText("Scores");
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showScores();
                }
            });
        } else if(animation == animDownMenu)
        {
            MyImageButton imgButton = findViewById(R.id.button_about);
            imgButton.setColor(Color.RED);
            imgButton.setFont(font);
            imgButton.setText("About");
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAbout();
                }
            });

            imgButton = findViewById(R.id.button_exit);
            imgButton.setColor(Color.BLUE);
            imgButton.setFont(font);
            imgButton.setText("Exit");
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
