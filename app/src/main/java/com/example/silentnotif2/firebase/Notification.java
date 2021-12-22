package com.example.silentnotif2.firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

    public String title;

    public String message;

    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
    }

    protected Notification(Parcel in) {
        title = in.readString();
        message = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
    }

    // another Parcelable stuff
}