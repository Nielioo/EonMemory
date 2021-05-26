package model;

import android.os.Parcel;
import android.os.Parcelable;

public class user implements Parcelable {
    String username, email, password;

    public user() {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public user(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    protected user(Parcel in) {
        username = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<user> CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel in) {
            return new user(in);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
    }
}
