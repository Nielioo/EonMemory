package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private int id;
    private String username, title, description, category, due_date, time, created, updated;

    public Task() {
        this.id = 0;
        this.username = "";
        this.title = "";
        this.description = "";
        this.category = "";
        this.due_date = "";
        this.time = "";
        this.created = "";
        this.updated = "";
    }

    public Task(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public Task(int id, String username, String title, String description, String category, String due_date, String time, String created, String updated) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.category = category;
        this.due_date = due_date;
        this.time = time;
        this.created = created;
        this.updated = updated;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        username = in.readString();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        due_date = in.readString();
        time = in.readString();
        created = in.readString();
        updated = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(due_date);
        dest.writeString(time);
        dest.writeString(created);
        dest.writeString(updated);
    }
}
