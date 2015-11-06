package com.edu.elon.deeznotes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by scottarmstrong on 10/21/15.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;

    private float downTouchX, downTouchY;
    private float upTouchX, upTouchY;
    private float moveTouchX;
    private float moveTouchY;
    private boolean moved;
    private Delete deleteButton;
    private int whichNote = -1;
    int highestSelected = -1;
    boolean wasTouched = false;
    protected long downTouch;
    protected long upTouch;

    //private Notes notes;
    private ArrayList<Note> noteArray;
    // URL -- phone must be connected to Elon's network
    private final String baseURL = "http://trumpy.cs.elon.edu:5000/notes";
    // values from EditTexts
    private String xText, yText, titleText, iDText;

    public GameLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        thread = new GameLoopThread();
    }

    public void setNotes(ArrayList<Note> noteArray) {
        this.noteArray = noteArray;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameLoopThread();
        }

        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // remember the last touch point

        int sizeOfArray = noteArray.size();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downTouchX = event.getX();
            downTouchY = event.getY();
            if (sizeOfArray > 0) {
                for (int i = 0; i < noteArray.size(); i++) {
                    if (downTouchX <= noteArray.get(i).x + (noteArray.get(i).width / 2) && downTouchX >= noteArray.get(i).x - (noteArray.get(i).width / 2) && downTouchY <= noteArray.get(i).y + (noteArray.get(i).height / 2) && downTouchY >= noteArray.get(i).y - (noteArray.get(i).height / 2)) {
                        highestSelected = i;
                        moveTouchX = downTouchX;
                        moveTouchY = downTouchY;
                        whichNote = i;
                        noteArray.get(i).isSelected = true;
                        wasTouched = true;
                    }
                }
            }
            moved = false;
            downTouch = System.currentTimeMillis();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (wasTouched) {

                moveTouchX = event.getX();
                moveTouchY = event.getY();
            }
            moved = true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            upTouchX = event.getX();
            upTouchY = event.getY();

            wasTouched = false;
            noteArray.get(highestSelected).isSelected = false;
//get last note, update the x and y location to send to the cloud

            xText = upTouchX + "";
            yText = upTouchY + "";


            if (upTouchX > deleteButton.x - deleteButton.width / 2
                    && upTouchX < deleteButton.x + deleteButton.width / 2
                    && upTouchY > deleteButton.y - deleteButton.height / 2
                    && upTouchY < deleteButton.y + deleteButton.height / 2) {


                // setup whichText
                //new DeleteNote().execute();

                // want to delete something?
                if (whichNote >= 0) {
                    noteArray.get(whichNote).delete();
                    whichNote = -1;
                }
            }
            upTouch = System.currentTimeMillis();
        }
        return true;

    }

    private class GameLoopThread extends Thread {

        private boolean isRunning = false;
        private long lastTime;

        private boolean intentsForIdiots = false;

        public GameLoopThread() {
            deleteButton = new Delete(context);
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {

            lastTime = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();
                    double elapsed = (now - lastTime) / 1000.0;
                    lastTime = now;

                    // update/draw
                    doUpdate(elapsed);
                    doDraw(canvas);

                    //updateFPS(now);
                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        // move all objects in the game
        private void doUpdate(double elapsed) {

            if (highestSelected >= 0 && moveTouchX != 0 && moveTouchY != 0) {

                noteArray.get(highestSelected).doUpdate(elapsed, moveTouchX, moveTouchY);

            }

            if (highestSelected >= 0 && noteArray.get(highestSelected).isSelected == false) {
                highestSelected = -1;
            }

        }

        // draw all objects in the game
        private void doDraw(Canvas canvas) {

            // draw the background
            canvas.drawColor(Color.argb(255, 126, 192, 238));
            deleteButton.doDraw(canvas);

            if (noteArray.size() > 0) {
                for (int i = 0; i < noteArray.size(); i++) {
                    noteArray.get(i).doDraw(canvas);
                }
            }
        }

        public String getStrings() {
            String butthead = new String("");
            //butthead = title + text;
            return butthead;

        }

        public Boolean booleanCheck() {
            return intentsForIdiots;
        }


    }

    /***
     * CUT HERE
     ***/



    /*
     * AsyncTask that connects to trumpy (webserver) to
     * call /add RESTful webservice.
     *
     * returns:
     *          "#" -- the note number if successful
     *          "invalid" -- if the add request was malformed
     *
     * (POST) http://trumpy.cs.elon.edu:5000/notes/add?x=13&y=67&note=This is my note
     */
    private class AddNote extends AsyncTask<Void, Void, Void> {

        private String response = "";

        @Override
        protected Void doInBackground(Void... voids) {
            titleText = "fuck";

            HttpURLConnection urlConnection = null;
            try {
                // build the URL
                String param = "x=" + URLEncoder.encode(xText, "UTF-8") +
                        "&y=" + URLEncoder.encode(yText, "UTF-8") +
                        "&title=" + URLEncoder.encode(titleText, "UTF-8") +
                        "&note=" + URLEncoder.encode(iDText, "UTF-8");
                URL url = new URL(baseURL + "/add?" + param);

                // connect to webserver (POST)
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");

                // read the response
                Scanner sc = new Scanner(urlConnection.getInputStream());
                response += sc.nextLine();

                publishProgress();

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                // always disconnect
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }


        /*
        * AsyncTask that connects to trumpy (webserver) to
        * call /add RESTful webservice.
        *
        * returns:
        *          "# deleted" -- the note was successful deleted
        *
        * (GET,POST) http://trumpy.cs.elon.edu:5000/delete/7
        */
        private class DeleteNote extends AsyncTask<Void, Void, Void> {

            private String response = "";

            @Override
            protected Void doInBackground(Void... voids) {
                HttpURLConnection urlConnection = null;
                try {
                    // connect to webserver (GET)
                    URL url = new URL(baseURL + "/delete/" + iDText);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // read the response
                    InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                    Scanner sc = new Scanner(in);
                    response = sc.nextLine();

                    // used to allow UI to update the TextView
                    publishProgress();

                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    // always disconnect
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return null;
            }


            /*
            * AsyncTask that connects to trumpy (webserver) to
            * call /add RESTful webservice.
            *
            * returns:
            *          "x,y
            *           note" -- the note
            *          "no note" -- if no note exists at given number
            *
            * (GET) http://trumpy.cs.elon.edu:5000/show/7
            */
            private class ShowNote extends AsyncTask<Void, Void, Void> {

                private String response = "";

                @Override
                protected Void doInBackground(Void... voids) {
                    HttpURLConnection urlConnection = null;
                    try {
                        // connect to webserver (GET)
                        URL url = new URL(baseURL + "/show/" + iDText);
                        System.out.println(iDText);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        // read the response
                        InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                        Scanner sc = new Scanner(in);
                        while (sc.hasNextLine()) {
                            response += sc.nextLine() + "\n";
                        }

                        // used to allow UI to update the TextView
                        publishProgress();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        // always disconnect
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                    return null;
                }


                /*
                * AsyncTask that connects to trumpy (webserver) to
                * call /add RESTful webservice.
                *
                * returns:
                *          "#
                 *          x,y
                 *          note text" -- all of the notes with note number
                *          "" -- if no notes exists
                *
                * (GET) http://trumpy.cs.elon.edu:5000/show_all
                */
                private class ShowAllNotes extends AsyncTask<Void, Void, Void> {

                    private String response = "";

                    @Override
                    protected Void doInBackground(Void... voids) {
                        HttpURLConnection urlConnection = null;
                        try {
                            // connect to webserver (GET)
                            URL url = new URL(baseURL + "/show_all");
                            urlConnection = (HttpURLConnection) url.openConnection();

                            // read the response
                            InputStream in = new BufferedInputStream((urlConnection.getInputStream()));
                            Scanner sc = new Scanner(in);
                            while (sc.hasNextLine()) {
                                response += sc.nextLine() + "\n";
                            }

                            // used to allow UI to update the TextView
                            publishProgress();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } finally {
                            // always disconnect
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                        }
                        return null;
                    }


                }
            }
        }
    }
}







