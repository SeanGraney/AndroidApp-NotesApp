package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView noteTitle;
    TextView saveDate;
    TextView noteText;

    public MyViewHolder(@NonNull View view) {
        super(view);
        noteTitle = view.findViewById(R.id.noteTitle);
        saveDate = view.findViewById(R.id.saveDate);
        noteText = view.findViewById(R.id.noteText);

    }

}
