package com.edu.elon.deeznotes;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;

public class NoteViewActivity extends Activity {

    String strings = new String("butt");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_note_view);
    }

    private void cancelButtonClick(View view) {
        Intent intent = new Intent();
        //intent = Intent.putExtra(this, NoteViewActivity.class);
    }

    public String getStrings(){
        return strings;
    }

    //Intent intent = new Intent(this, NoteViewActivity.class);
    //intent.putExtra("strings", this.getStrings());
    //startActivityForResult(intent, WIDTH_DIALOG);
}
