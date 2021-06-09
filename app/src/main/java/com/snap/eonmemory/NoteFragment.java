package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import model.Note;
import model.OnCardClickListener;
import model.setRefresh;

public class NoteFragment extends Fragment implements OnCardClickListener, setRefresh {

    View view;
    RecyclerView note_recyclerView;
    SwipeRefreshLayout note_swipeRefresh;
    FloatingActionButton note_FAB_create;
    SearchView note_search_input;
    ArrayList<Note> noteList;
    NoteRVAdapter adapter;

    Query noteReference;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_note, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        initialize();

        loadNote();
        setSearch();

        setListener();
        setSwipeRefresh();

        return view;
    }

    private void setSearch() {
        if(note_search_input != null){
            note_search_input.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText != null){
                        adapter.getFilter().filter(newText);
                    }

                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(int position) {
        Note note = noteList.get(position);
        String noteId = note.NoteId;

        Intent intent = new Intent(getContext(), EditNoteActivity.class);
        intent.putExtra("noteId", noteId);
        startActivity(intent);

        note_search_input.clearFocus();
        note_search_input.setQuery("", false);
    }

    @Override
    public void setSwipeRefresh() {
        note_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noteList.clear();
                loadNote();

                note_swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void setListener() {
        note_FAB_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                CreateNoteFragment createNote = new CreateNoteFragment(NoteFragment.this);
                createNote.show(fm, null);
            }
        });
    }


    private void loadNote() {
        noteReference = fStore.collection("user_collection")
                .document(userID).collection("note_collection")
                .orderBy("created", Query.Direction.DESCENDING);

        noteReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                noteList.clear();

                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        String id = doc.getId();
                        Note note = doc.toObject(Note.class).withId(id);

                        noteList.add(note);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initialize() {
        note_recyclerView = view.findViewById(R.id.note_recyclerView);
        note_swipeRefresh = view.findViewById(R.id.note_swipeRefresh);
        note_FAB_create = view.findViewById(R.id.note_FAB_create);
        note_search_input = view.findViewById(R.id.note_search_input);
        noteList = new ArrayList<Note>();
        adapter = new NoteRVAdapter(noteList, this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        note_recyclerView.setLayoutManager(manager);
        note_recyclerView.setAdapter(adapter);
    }
}