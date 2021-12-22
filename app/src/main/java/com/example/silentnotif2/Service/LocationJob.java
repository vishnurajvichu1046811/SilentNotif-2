package com.example.silentnotif2.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.silentnotif2.Utility.ClsGlobal;
import com.example.silentnotif2.firebase.FirebaseRealtimeDB;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LocationJob extends JobService {

    private static final String TAG = LocationJob.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Service created");

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob");

        String str = "";
        GPSTracker gps = new GPSTracker(this);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            str ="Your Locion is - \nLat: " + latitude
                    + "\nLog: " + longitude;


            FirebaseRealtimeDB realtimeDB = new FirebaseRealtimeDB();
            realtimeDB.addLocation("user1",latitude+"",longitude+"", ClsGlobal.getCurruntDateTime());

        }

        gps.stopUsingGPS();

        jobFinished(params,true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobFinished(params,true);
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service destroyed");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        //return START_NOT_STICKY;
        return START_STICKY;
    }


}
