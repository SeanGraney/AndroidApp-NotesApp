package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "NotesAdapter";
    private List<Notes> noteList;
    private MainActivity mainAct;

    NotesAdapter(List<Notes> noteList, MainActivity ma){
        this.noteList = noteList;
        mainAct = ma;
    }


//    creates visual layout object
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflatedLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false);

//        makes the recycler list tap and long tapable
        inflatedLayout.setOnClickListener(mainAct);
        inflatedLayout.setOnLongClickListener(mainAct);

        return new MyViewHolder(inflatedLayout);
    }


//    sets text in view holder in index position
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Notes n = noteList.get(position);
        String displayTitle = n.getTitle();
        String displayNote = n.getNotes();

        if (displayTitle.length() >80) {
            displayTitle = displayTitle.substring(0,81)+"...";
        }
        if (displayNote.length() >80) {
            displayNote = displayNote.substring(0,81)+"...";
        }

        holder.noteTitle.setText(displayTitle);
        holder.saveDate.setText(String.valueOf(n.getLastSave()));
        holder.noteText.setText(displayNote);

    }


//    Returns size of list
    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
