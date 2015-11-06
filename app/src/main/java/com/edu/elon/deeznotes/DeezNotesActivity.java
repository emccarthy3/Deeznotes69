package com.edu.elon.deeznotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DeezNotesActivity extends Activity {

    private TextView resultText;
    private Notes notes;
    Context context;
    //private GameLoopView boots = new GameLoopView(context); //AttributeSet attrs);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deez_notes);


        GameLoopView view = (GameLoopView) findViewById(R.id.view);
        notes = new Notes();
        view.setNotes(notes.noteListGetter());
        //view.setNotes(notes);
        context = getApplicationContext();

    }

    public void onClick(View view) {
        //showInputDialog();
        notes.notesArray.add(new Note(context));
        // we need to add A NOTE TO THE CLOOUUUDDDD :D :( :,/ HOWDOWEDO

    }

    public void inputChecker(){

    }

}
