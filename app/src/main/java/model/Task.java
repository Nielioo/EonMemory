package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private int id;
    private String title, description, category, due_date, time, created, updated;

    public Task() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.category = "";
        this.due_date = "";
        this.time = "";
        this.created = "";
        this.updated = "";
    }

    public Task(int id, String title, String description, String category, String due_date, String time, String created, String updated) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(due_date);
        dest.writeString(time);
        dest.writeString(created);
        dest.writeString(updated);
    }
}
