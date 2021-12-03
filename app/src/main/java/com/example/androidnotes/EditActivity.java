package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private Notes noteObj;
    private final ArrayList<Notes> noList = new ArrayList<>();
    private boolean newNote;
    EditText editTitle;
    EditText editNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Log.d(TAG, "onCreate called");

        editTitle = findViewById(R.id.editTitle);
        editNotes = findViewById(R.id.editNotes);


//        sets note and title fields if editing an existing note
        Intent obj = getIntent();  // Used to hold data to be returned to other activity
        if (obj.hasExtra("NoteObj")) {
            noteObj = (Notes) obj.getSerializableExtra("NoteObj");
            newNote = false;
            Log.d(TAG, "Note Obj Passed correctly" +noteObj.toString());
            if (noteObj != null){
                editTitle.setText(noteObj.getTitle());
                editNotes.setText(noteObj.getNotes());
            }
        } else{
            newNote = true;

        }

    }

    //    attaches edit menu on the main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //  finds the menue item selected and carries out task
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent data = new Intent();  // Used to hold data to be returned to other activity
        Intent n = getIntent();

        if (item.getItemId() == R.id.saveButton) {
            Log.d(TAG, "Save Button Selected");
            int size = n.getIntExtra("listSize", 0);
            Log.d(TAG, "first group " +size);
//            if (size == 0) {
//                Log.d(TAG, "first group running" +size);
//                data.putExtra("Title", editTitle.getText().toString());
//                data.putExtra("Notes", editNotes.getText().toString());
//                data.putExtra("newNote", newNote);
//
//                setResult(RESULT_OK, data);
//                finish(); //closes current activity
//            }
            if (!newNote) {
                if ((noteObj.getNotes().equals(editNotes.getText().toString())) && (noteObj.getTitle().equals(editTitle.getText().toString()))) {
                    finish();
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (editTitle.getText().toString().isEmpty()) {
                //        Alert box asking if you would like to exit without saving
                builder.setPositiveButton("OK", (dialog, id) -> {
                    Log.d(TAG, "Exit attempted without title - selected ok");
                    finish(); //closes current activity
                });

                builder.setNegativeButton("CANCEL", (dialog, id) -> {});
                builder.setTitle("Your note may not be saved without a title");
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {

                data.putExtra("Title", editTitle.getText().toString());
                data.putExtra("Notes", editNotes.getText().toString());
                data.putExtra("newNote", newNote);

                setResult(RESULT_OK, data);
                finish(); //closes current activity
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back button pressed");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (editTitle.getText().toString().isEmpty()) {
            //        Alert box asking if you would like to exit without saving
            builder.setPositiveButton("OK", (dialog, id) -> {
                Log.d(TAG, "Exit attempted without title - selected ok");
                finish(); //closes current activity
            });

            builder.setNegativeButton("CANCEL", (dialog, id) -> {});

            builder.setTitle("Your note may not be saved without a title");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if ((noteObj.getNotes().equals(editNotes.getText().toString())) && (noteObj.getTitle().equals(editTitle.getText().toString()))) {
            finish();
        } else {
                //        Alert box asking if you would like to exit without saving
                builder.setPositiveButton("Save", (dialog, id) -> {
                    Log.d(TAG, "Save Selected from dialog");

                    if ((noteObj.getNotes().equals(editNotes.getText().toString())) && (noteObj.getTitle().equals(editTitle.getText().toString()))) {
                        finish();
                    }

                    Intent data = new Intent();  // Used to hold data to be returned to other activity
                    data.putExtra("Title", editTitle.getText().toString());
                    data.putExtra("Notes", editNotes.getText().toString());
                    data.putExtra("newNote", newNote);

                    setResult(RESULT_OK, data);
                    finish(); //closes current activity
                });

            builder.setNegativeButton("CANCEL", (dialog, id) -> {finish();});

            builder.setTitle("Would you lke to save this note?");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}