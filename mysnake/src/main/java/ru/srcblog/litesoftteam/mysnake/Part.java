package ru.srcblog.litesoftteam.mysnake;

import java.util.Formatter;

/**
 * Created by javavirys on 21.06.2017.
 */

public class Part {

    public static final int MOVE_UP = 2;
    public static final int MOVE_DOWN = 8;
    public static final int MOVE_LEFT = 4;
    public static final int MOVE_RIGHT = 6;

    int x;
    int y;
    int w;
    int h;


    int xBlock;
    int yBlock;
    int wBlock;
    int hBlock;

    public Part()
    {
        this(0,0,0,0,0);
    }

    public Part(int xBlock,int yBlock,int wBlock,int hBlock,int BlockCount)
    {
        this.xBlock = xBlock;
        this.yBlock = yBlock;
        this.wBlock = wBlock;
        this.hBlock = hBlock;
    }

    public Part(Part part)
    {
        //this(part.getX(),part.getY(),part.getW(),part.getH());
        this(part.getXBlock(),part.getYBlock(),part.getWBlock(),part.getHBlock(),0);
    }

    public int getXBlock()
    {
        return xBlock;
    }

    public int getYBlock()
    {
        return yBlock;
    }

    public int getWBlock()
    {
        return wBlock;
    }

    public int getHBlock()
    {
        return hBlock;
    }


    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getW()
    {
        return w;
    }

    public int getH()
    {
        return h;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public void setH(int h)
    {
        this.h = h;
    }



    @Override
    public String toString()
    {
        Formatter f = new Formatter();
        f.format("Part params: xBlock=%d yBlock=%d wBlock=%d hBlock=%d",new Integer(xBlock),new Integer(yBlock),
                new Integer(wBlock),new Integer(hBlock));
        return f.toString();
    }

}
