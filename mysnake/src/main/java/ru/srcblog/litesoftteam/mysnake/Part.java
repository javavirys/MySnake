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

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by javavirys on 21.06.2017.
 */

public class Part {

    public static final int TRANS_NONE          = 10;
    public static final int TRANS_ROT90         = 11;
    public static final int TRANS_ROT180        = 12;
    public static final int TRANS_ROT270        = 13;
    public static final int TRANS_MIRROR        = 14;
    public static final int TRANS_MIRROR_ROT90  = 15;
    public static final int TRANS_MIRROR_ROT180 = 16;
    public static final int TRANS_MIRROR_ROT270 = 17;

    public static final int MOVE_UP = 2;
    public static final int MOVE_DOWN = 8;
    public static final int MOVE_LEFT = 4;
    public static final int MOVE_RIGHT = 6;


    private boolean isVisible;

    /*
        Координаты квадрата в пикселях
     */
    int x;
    int y;
    int w;
    int h;

    /*
        Координаты квадрата на сетке
     */
    int xBlock;
    int yBlock;
    int wBlock;
    int hBlock;

    /*
        Индекс frame
     */
    int frameIndex;
    int defaultFrame;

    ArrayList<Bitmap> frames;

    Matrix matrix;

    int transform;

    int direction; // Дополн. переменная чтобы знать куда движемся

    int frameAdd = 1;

    int frameSequence[]; // последовательность отображения
    int sequenceIndex = 0;

    Bitmap swapBmp = null;

    boolean deletedFlag;

    public int oldX;
    public int oldY;
    public int oldDir;

    public Part(int xBlock, int yBlock, int wBlock, int hBlock, Bitmap bitmaps[],int direction)
    {
        isVisible = true;
        this.xBlock = xBlock;
        this.yBlock = yBlock;
        this.wBlock = wBlock;
        this.hBlock = hBlock;

        frames = new ArrayList<>();

        if (bitmaps != null) {
            for (Bitmap bitmap : bitmaps)
                frames.add(bitmap);
        }

        defaultFrame = 0;
        frameIndex = 0;

        matrix = new Matrix();

        this.direction = direction;

        frameSequence = null;
    }

    public Part(int xBlock, int yBlock, int wBlock, int hBlock, Bitmap bitmaps[])
    {
        this(xBlock,yBlock,wBlock,hBlock,bitmaps,MOVE_RIGHT);
    }

    public Part(Part part)
    {
        this(part.getXBlock(),part.getYBlock(),part.getWBlock(),part.getHBlock(),
                part.getBitmaps());
        setDirection(part.getDirection());
    }

    public Part(int xBlock, int yBlock, int wBlock, int hBlock, ArrayList<Bitmap> bitmaps)
    {
        this(xBlock,yBlock,wBlock,hBlock,null,MOVE_RIGHT);
        setBitmaps(bitmaps);
    }

    public Part()
    {
        this(0,0,0,0,null,MOVE_RIGHT);
    }


    public boolean isVisible() {
        return isVisible;
    }

    public boolean isDeleted()
    {
        return deletedFlag;
    }

    public int getFrame() {
        return frameIndex;
    }

    public int getDefaultFrame() { return defaultFrame; }

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


    /**
     *
     * @return Список bitmaps
     */
    public ArrayList<Bitmap> getBitmaps() {
        return frames;
    }


    /**
     * Получить выбранную картинку
     * @return Текущая bitmap
     * @see #setFrame(int)
     * @see #getFrame()
     */
    public Bitmap getBmp()
    {
        if(swapBmp != null)
        {
            return swapBmp;
        }
        Bitmap bmp = frames.get(frameIndex);
        bmp = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
        return bmp;
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

    public int getFramesCount()
    {
        return frames.size();
    }

    public Bitmap getSwapBmp() {
        return swapBmp;
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

    public int getTransform()
    {
        return transform;
    }

    public int getDirection()
    {
        return direction;
    }

    public void setTransform(int trans)
    {
        //Log.d(MainCanvas.LOG_NAME,"setTranform: " + trans);
        transform = trans;
        matrix.reset();
        switch (transform)
        {
            case TRANS_NONE:
                matrix.postRotate(0);
                break;
            case TRANS_ROT90:
                matrix.postRotate(90);
                break;
            case TRANS_ROT180:
                matrix.postRotate(180);
                break;
            case TRANS_ROT270:
                matrix.postRotate(270);
                break;
            case TRANS_MIRROR:
                matrix.postScale(-1,-1);
                break;
            case TRANS_MIRROR_ROT90:
            case TRANS_MIRROR_ROT180:
            case TRANS_MIRROR_ROT270:
                matrix.postScale(-1,-1);
                break;
        }
    }

    public void setBitmaps(ArrayList<Bitmap> arr) {
        frames.clear();
        frames.addAll(arr);
    }

    public void setDefaultFrame(int index)
    {
        defaultFrame = index;
    }

    public void setFrame(int index)
    {
        frameIndex = index;
    }

    public void setDir(int dir)
    {
        direction = dir;
    }

    public void setDirection(int dir)
    {
        direction = dir;
        switch (direction)
        {
            case MOVE_RIGHT:
                setTransform(TRANS_NONE);
                break;
            case MOVE_DOWN:
                setTransform(TRANS_ROT90);
                break;
            case MOVE_LEFT:
                setTransform(TRANS_ROT180);
                break;
            case MOVE_UP:
                setTransform(TRANS_ROT270);
                break;
        }

    }

    public void setFrameSequence(int sequence[])
    {
        frameSequence = sequence;
    }

    public void setVisible(boolean flag)
    {
        isVisible = flag;
    }

    /**
     * Меняем текстуру на заданую
     * @param bmp - текстура, если null то все возвращается
     */
    public void setSwapBmp(Bitmap bmp)
    {
        swapBmp = bmp;
    }

    public void setDeletedFlag(boolean flag)
    {
        deletedFlag = flag;
    }

    public void resetTransformBmp() {
        frameIndex = defaultFrame;
    }

    public void nextFrame()
    {
        int index = 0;
        if(frameSequence == null) {
            index = getFrame();

            index += frameAdd;
            if (index > getFramesCount() - 1) {
                index = 0;
            }
            setFrame(index);
        } else
        {
            index = nextSequence();

        }
        setFrame(index);
    }

    private int nextSequence()
    {
        return frameSequence[sequenceIndex >= frameSequence.length ?
                sequenceIndex = 0 : sequenceIndex ++];
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
