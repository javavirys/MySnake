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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import ru.srcblog.litesoftteam.mysnake.MainActivity;
import ru.srcblog.litesoftteam.mysnake.MainCanvas;
import ru.srcblog.litesoftteam.mysnake.R;

public class MenuActivity extends Activity {

    MenuActivity main;

    InterstitialAd mInterstitialAd = null;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;
        setContentView(R.layout.activity_menu);

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

        AdRequest adRequest = new AdRequest.Builder()//.addTestDevice("8C42340767A25C05F0527E18EC82509B")
                .build();

        mInterstitialAd.loadAd(adRequest);

        ImageView menu = findViewById(R.id.menu_view);
        menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Rect rect[][] = new Rect[2][2];

                for(int i = 0; i < 2; i++)
                    for(int j = 0; j < 2; j++)
                    {
                        rect[i][j] = new Rect(i * (view.getWidth() / 2),j * (view.getHeight() / 2),
                                    i * (view.getWidth() / 2) + (view.getWidth() / 2),
                                j * (view.getHeight() / 2) + (view.getHeight() / 2));
                    }

                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();

                System.out.println("Pos: " + x + " " + y);
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                if(rect[0][0].contains(x,y)) {
                    System.out.println("Click");
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
                } else if(rect[1][0].contains(x,y)) {
                    Intent i = new Intent(getApplicationContext(), ScoresActivity.class);
                    startActivity(i);
                } else if(rect[0][1].contains(x,y))
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
                } else if(rect[1][1].contains(x,y))
                {
                    finish();
                }
                return true;
            }
        });
    }

}
