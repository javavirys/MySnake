package ru.srcblog.litesoftteam.mysnake.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ru.srcblog.litesoftteam.mysnake.R;

public class ScoresActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        ListView l = findViewById(R.id.list_view_scores);
        ScoresAdapter adapter = new ScoresAdapter(this);
        l.setAdapter(adapter);

        File cache = getCacheDir();
        cache = new File(cache,"scores.xml");

        Intent i = getIntent();
        String sName = i.getStringExtra("score_name");
        int iValue = i.getIntExtra("score_value",-1);

        Log.d("TEST",cache.toString());

        if(sName != null && iValue != -1) {

            ItemAdapter item = new ItemAdapter();
            item.title = sName;
            item.value = iValue;
            //adapter.addItem(item);

            writeScores(cache,item); // пишем наши достижения
        }

        readScores(cache,adapter);

        adapter.notifyDataSetChanged();
    }

    public void readScores(File filePath,ScoresAdapter adapter)
    {
        try {
            Scanner sc = new Scanner(filePath);
            while(sc.hasNextLine()) {
                String text = sc.nextLine();
                if(!text.isEmpty())
                {
                    // парсим строку
                    String[] tmp = text.split("&&");
                    if(tmp.length >= 2) {
                        ItemAdapter item = new ItemAdapter();
                        item.title = tmp[0];
                        item.value = Integer.parseInt(tmp[1]);
                        adapter.addItem(item);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeScores(File filePath,ItemAdapter item)
    {
        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePath,true);
            fos.write(new String(item.title + "&&" + item.value + "\n").getBytes("UTF-8"));
            fos.close();

            //filePath.delete();

        }catch (Exception e){
            Log.d("TEST",e.toString());
        }
    }

}
