package com.snap.eonmemory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import model.Note;
import model.OnCardClickListener;
import model.Task;

public class NoteRVAdapter extends RecyclerView.Adapter<NoteRVAdapter.NoteViewHolder> {

    private ArrayList<Note> noteList;
    private OnCardClickListener cardListener;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public NoteRVAdapter(ArrayList<Note> noteList, OnCardClickListener cardListener) {
        this.noteList = noteList;
        this.cardListener = cardListener;
    }

    @NonNull
    @Override
    public NoteRVAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_note, parent, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteRVAdapter.NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.cardView_note_title_textView.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView cardView_note_title_textView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView_note_title_textView = itemView.findViewById(R.id.cardView_note_title_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardListener.onClick(getAdapterPosition());
                }
            });

        }
    }
}
