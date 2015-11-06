package com.edu.elon.deeznotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by scottarmstrong on 10/27/15.
 */
public class Add {
    protected float x, y;
    protected float width, height;
    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    private final float SCALE = 1.5f;

    public Add(Context context) {

        // get the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.add_button);

        // scale the size
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        x = width;
        y = screenHeight - height;
    }

    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width / 2), (int) (y - height /2),
                        (int) (x + width / 2), (int) (y + height /2)),
                null);
    }

}
