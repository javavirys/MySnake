package ru.srcblog.litesoftteam.mysnake.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import ru.srcblog.litesoftteam.mysnake.R;

public class SplashActivity extends Activity implements Animation.AnimationListener{

    Animation animLogo,animText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView logo = findViewById(R.id.splash);
        animLogo = AnimationUtils.loadAnimation(this,R.anim.anim_splash_text);
        animLogo.setAnimationListener(this);
        logo.startAnimation(animLogo);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent menuIntent = new Intent(getApplicationContext(),MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
