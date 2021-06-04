package com.snap.eonmemory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.setRefresh;

public class CreateNoteFragment extends BottomSheetDialogFragment{

    View view;
    TextInputLayout createNote_title_textInput;
    ImageView createNote_save_imageView;

    boolean validateTitle;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    private setRefresh refresh;

    public CreateNoteFragment(setRefresh refresh) {
        this.refresh = refresh;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_create_note, container, false);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        initialize();
        setListener();

        return view;
    }

    private void createNote(String title) {
        CollectionReference noteReference = fStore.collection("user_collection")
                .document(userID).collection("note_collection");

        Map<String, Object> note = new HashMap<>();
        note.put("title", title);
        note.put("description", "");
        note.put("created", FieldValue.serverTimestamp());

        noteReference.add(note).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", e.toString());
            }
        });

        dismiss();
    }

    private void isSaveValid(boolean validateTitle) {
        createNote_save_imageView.setEnabled(validateTitle);
    }

    private void setListener(){
        createNote_title_textInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = createNote_title_textInput.getEditText().getText().toString().trim();

                if (title.isEmpty()) {
                    validateTitle = false;
                } else {
                    createNote_title_textInput.setError(null);
                    validateTitle = true;
                }

                isSaveValid(validateTitle);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createNote_save_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = createNote_title_textInput.getEditText().getText().toString().trim();

                createNote(title);

                refresh.setSwipeRefresh();
            }
        });
    };

    private void initialize() {
        createNote_title_textInput = view.findViewById(R.id.createNote_title_textInput);
        createNote_save_imageView = view.findViewById(R.id.createNote_save_imageView);

        createNote_title_textInput.requestFocus();
        createNote_save_imageView.setEnabled(false);
    }
}