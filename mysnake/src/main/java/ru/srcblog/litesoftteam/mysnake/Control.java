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

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by javavirys on 22.06.2017.
 */

public class Control {

    Paint paint;

    int x;
    int y;
    int r;

    public Control(int x, int y, int r)
    {
        this.x = x;
        this.y = y;
        this.r = r;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x45000000);

    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x,y,r,paint);
        canvas.drawCircle(x,y,r / 2,paint);
    }


}
