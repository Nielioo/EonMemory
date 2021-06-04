package model;

import com.google.firebase.Timestamp;

public class Note extends NoteId{
    private String title, description;
    private Timestamp created, updated;

    public Note() {
        this.title = "";
        this.description = "";
    }

    public Note(String title, String description, Timestamp created, Timestamp updated) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}

