package model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class NoteId {
    @Exclude
    public String NoteId;

    public <T extends NoteId> T withId(@NonNull final String id) {
        this.NoteId = id;
        return (T) this;
    }
}
