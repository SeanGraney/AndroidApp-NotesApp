package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";

    private final ArrayList<Notes> notesList = new ArrayList<>();
    private int pos;
    private NotesAdapter mAdapter;
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Change action bar title
//        getActionBar().setTitle("Hello world App");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Android Notes (" + notesList.size() + ")");  // provide compatibility to all the versions

        recyclerView = findViewById(R.id.recycler);

//        Data to recyclerView Adapter
        mAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        data returned from another activity
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

        notesList.addAll(loadFile());
    }



    @Override
    protected void onResume() {
//        notesList.clear();
        notesList.addAll(loadFile());
        super.onResume();
    }


    private ArrayList<Notes> loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        ArrayList<Notes> noteList = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d(TAG, "While loop complete");

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String notes = jsonObject.getString("notes");
                String lastSave = jsonObject.getString("lastSave");
                Notes note = new Notes(title, notes);
                noteList.add(note);
            }

        } catch (FileNotFoundException e) {
            Log.d(TAG, getString(R.string.no_file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noteList;
    }

    @Override
    protected void onPause() {
        saveNotes();
        super.onPause();
    }

    private void saveNotes() {

        Log.d(TAG, "saveNote: Saving JSON File");

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("Notes.jason", Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(notesList);
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveNotes: JSON:\n" + notesList.toString());

            Log.d(TAG, "saved");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }



//    attaches main menu on the main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



//  finds the menue item selected and carries out task
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addNoteButton) {
            Log.d(TAG, "Add Note Button Selected");
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("listSize", notesList.size());
            activityResultLauncher.launch(intent);
        } else if (item.getItemId() == R.id.infoButton) {
            Log.d(TAG, "info Button Selected");
            Intent intent2 = new Intent(this, AboutActivity.class);
            startActivity(intent2);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "On Click Listener ");
        openNewActivity(view);
    }


//    Dialog delete warning
    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, "On Long Click Listener ");
        pos = recyclerView.getChildLayoutPosition(view);

//        Alert box asking for removal confirmation of object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", (dialog, id) -> {
            notesList.remove(pos);
            mAdapter.notifyItemRemoved(pos);
        });

        builder.setNegativeButton("CANCEL", (dialog, id) -> {});

        builder.setTitle("Are you sure you want to delete note: " + notesList.get(pos).getTitle());

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }


    public void openNewActivity(View v){
        pos = recyclerView.getChildLayoutPosition(v);

//        passes in clicked note obj
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NoteObj", notesList.get(pos));
        Log.d(TAG, "Note Obj Passed " +notesList.get(pos).toString());
        activityResultLauncher.launch(intent);
    }

    public void openAboutActivity(View v){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void handleResult(ActivityResult result){
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult Recieved");
            return;
        }

        Intent data = result.getData();
        if(result.getResultCode() == RESULT_OK) {
            String title = data.getStringExtra("Title");
            String notes = data.getStringExtra("Notes");
            boolean newNote = data.getBooleanExtra("newNote", false);
//            if (mAdapter.getItemCount() == 0) {
//                notesList.add(new Notes(title, notes));
//            }

            if (title == null) {
                Log.d(TAG, "Title field returned null");
                return;
            }
            if (title.isEmpty()) {
                Log.d(TAG, "Title field returned Empty Text");
                return;
            }

            if (notes == null) {
                Log.d(TAG, "Notes field returned null");
            }
            if (notes.isEmpty()) {
                Log.d(TAG, "Notes field returned Empty Text");
            }
            if (newNote) {
                Log.d(TAG, "Note is a new entry");
                notesList.add(new Notes(title, notes));
            } else {
                Log.d(TAG, "Note is an old entry to be updated");
                notesList.get(pos).setTitle(title);
                notesList.get(pos).setNotes(notes);
                notesList.get(pos).setLastSave();
                notesList.get(pos).setCmpDate();
                mAdapter.notifyItemInserted(pos);
            }
            notesList.sort(Comparator.comparing(Notes::getCmpDate).reversed());
            mAdapter.notifyDataSetChanged();
        }
    }

}