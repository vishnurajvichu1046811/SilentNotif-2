package com.example.silentnotif2.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.silentnotif2.models.LocationData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRealtimeDB {

    public void addLocation(String userName,String latitude,String longitude,String dateTime){
        if(latitude.equalsIgnoreCase("0.0") || longitude.equalsIgnoreCase("0.0"))
            return;

        LocationData locationData = new LocationData(userName,latitude,longitude,dateTime);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("location-android");
        usersRef.child("location").push().setValue(locationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Loc details added successfully.");
                } else {
                    Log.e("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });
    }

}
