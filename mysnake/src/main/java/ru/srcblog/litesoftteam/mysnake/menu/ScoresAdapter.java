package ru.srcblog.litesoftteam.mysnake.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.srcblog.litesoftteam.mysnake.R;

/**
 * Created by javavirys on 27.06.2017.
 */

public class ScoresAdapter extends BaseAdapter {

    private Context context;
    ArrayList<ItemAdapter> list;

    public ScoresAdapter(Context c)
    {
        context = c;
        list = new ArrayList<>();
    }

    public void addItem(ItemAdapter item)
    {
        list.add(item);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;
        if(view == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.list_view_view,null);
            ((TextView)v.findViewById(R.id.view_title)).setText(list.get(position).title);
            ((TextView)v.findViewById(R.id.view_value)).setText(String.valueOf(list.get(position).value));
        }else
            v = view;
        return v;
    }

}

class ItemAdapter{
    public String title;
    public int value;

}