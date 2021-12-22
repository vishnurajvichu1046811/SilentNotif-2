package com.example.silentnotif2.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.silentnotif2.Utility.ClsGlobal;

public class LocationWorker extends Worker {
    private final Context context;
    private boolean CheckNewWorker = false;

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        Log.e("LocationWorker", "LocationWorker call");
    }


    @NonNull
    @Override
    public Result doWork() {
        Log.e("LocationWorker", "doWork call");

        /**
         * This is to get location from background every haft an Hour in day time.
         * In night time after 8 pm we will get Location every 2 Hour.
         */
        int getCurrentHour = ClsGlobal.getCurrentHour();

        if (getCurrentHour >= 8 && getCurrentHour <= 20) {

            if (ClsGlobal.isWorkScheduled(getApplicationContext().getPackageName().concat(".Location.120min"))) {
                Log.e("LocationWorker", ".Location.120min cancel");
                ClsGlobal.CancelWorkByTag(getApplicationContext().getPackageName().concat(".Location.120min"));
            }
            if (ClsGlobal.isWorkScheduled(getApplicationContext().getPackageName().concat(".Location.16Minute"))) {
                Log.e("LocationWorker", "..Location.16Minute cancel");
                ClsGlobal.CancelWorkByTag(getApplicationContext().getPackageName().concat(".Location.16Minute"));
            }

            if (!ClsGlobal.isWorkScheduled(getApplicationContext().getPackageName().concat(".Location.30min"))) {
                Log.e("LocationWorker", ".Location.30min ScheduleWorker");
                CheckNewWorker = true;
                ClsGlobal.ScheduleWorker(getApplicationContext().getPackageName().concat(".Location.30min"), 30);
            }

            Log.e("LocationWorker", "30 min");

        } else {

            if (ClsGlobal.isWorkScheduled(getApplicationContext().getPackageName().concat(".Location.30min"))) {
                Log.e("LocationWorker", ".Location.30min cancel");
                ClsGlobal.CancelWorkByTag(getApplicationContext().getPackageName().concat(".Location.30min"));
            }

            if (!ClsGlobal.isWorkScheduled(getApplicationContext().getPackageName().concat(".Location.120min"))) {
                Log.e("LocationWorker", ".Location.120min ScheduleWorker");
                CheckNewWorker = true;
                ClsGlobal.ScheduleWorker(getApplicationContext().getPackageName().concat(".Location.120min"), 120);
            }

            Log.e("LocationWorker", "120 min");
        }


        if (!CheckNewWorker){
            Intent intent = new Intent(context, LocationBroadcastReceiver.class);
            context.sendBroadcast(intent);
        }


        /**
         * This is to get location from background every Hour.
         *
         */
        Intent intent = new Intent(context, LocationBroadcastReceiver.class);
        context.sendBroadcast(intent);


        return Result.success();
    }
}
