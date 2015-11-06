package com.edu.elon.deeznotes;

import java.util.ArrayList;

/**
 * Created by scottarmstrong on 10/29/15.
 */
public class Notes {

    ArrayList<Note> notesArray = new ArrayList<Note>();

    public void addNote(Note note){
        notesArray.add(note);
    }
    public ArrayList<Note> getNotesArray(){
        return notesArray;
    }

    //public Notes() {

        //loadNotesFromCloud();

    //}

    public ArrayList<Note> noteListGetter() {
        return notesArray;
    }

    private void loadNotesFromCloud() {

    }
}
