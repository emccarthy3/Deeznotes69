package com.edu.elon.deeznotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * Created by scottarmstrong on 10/22/15.
 */
public class Delete {

    protected float x, y;
    protected float width, height;
    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    private final float SCALE = 1.5f;

    public Delete(Context context) {

        // get the image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.delete_button);

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


        x = screenWidth - width;
        y = screenHeight - height;
    }

    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width / 2), (int) (y - height /2),
                        (int) (x + width / 2), (int) (y + height /2)),
                null);
    }

    //get the x value of the location of the trash icon
    public float getTrashX(){
        return x-width;
    }

    //gets the y value of the location of the trash icon
    public float getTrashY(){
        return y-height;
    }

    //remove note from the array
    public void removeNoteFromArray(Note note, Note [] notes){ //@FIXME ArrayList instead?
        for(int i = 0; i < notes.length; i++){
            if (notes[i].equals(note)){
                notes[i]= null;
            }
        }
    }


}
