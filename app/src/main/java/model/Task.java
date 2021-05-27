package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private int id;
    private String title, description, category, due_date, time;

    public Task() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.category = "";
        this.due_date = "";
        this.time = "";
    }

    public Task(int id, String title, String description, String category, String due_date, String time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.due_date = due_date;
        this.time = time;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        due_date = in.readString();
        time = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    }
}
