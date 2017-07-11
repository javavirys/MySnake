package ru.srcblog.litesoftteam.mysnake.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import ru.srcblog.litesoftteam.mysnake.MainActivity;
import ru.srcblog.litesoftteam.mysnake.R;

public class MenuActivity extends Activity {

    MenuActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;
        setContentView(R.layout.activity_menu);

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(main);
                d.setContentView(R.layout.layout_start_dialog);
                d.setTitle("Play setting");

                d.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        SeekBar bar = d.findViewById(R.id.seek_high_level);

                        i.putExtra(MainActivity.INTENT_MSG_DIFFICULTY,bar.getProgress());
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
        });

        findViewById(R.id.button_scores).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ScoresActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.button_help).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
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
        });


        findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
