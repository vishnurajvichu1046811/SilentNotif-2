package com.example.silentnotif2.Utility;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.silentnotif2.Service.LocationJob;
import com.example.silentnotif2.Service.LocationWorker;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ClsGlobal {


    public static int getCurrentHour(){
        Calendar rightNow = Calendar.getInstance();
        // return the hour in 24 hrs format (ranging from 0-23);
        return rightNow.get(Calendar.MINUTE);
    }


    public static String getEntryDateFormat(String e_Date) {
        final String DATE_DASH_FORMAT = "dd/MM/yyyy hh:mm aa";
        final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
        try {
            if (e_Date != null && !e_Date.isEmpty() && e_Date != "") {
                Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(e_Date);
                DateFormat formatter = new SimpleDateFormat(DATE_DASH_FORMAT, Locale.getDefault());
                e_Date = formatter.format(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return e_Date;
    }


    public static String getCurruntDateTime() {
        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static boolean isWorkScheduled(String tag) {
        WorkManager instance = WorkManager.getInstance();
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CancelWorkByTag(String tag){
        WorkManager.getInstance().cancelAllWorkByTag(tag);
    }

    public static void ScheduleWorker(String tagName,int min){

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(LocationWorker.class,
                min, TimeUnit.MINUTES)
                .addTag(tagName)
                .build();

        // For Setting up Unique PeriodicWork. So there is one PeriodicWork active at a time.
        // Remember there is Only one PeriodicWork at a time.
        WorkManager.getInstance().enqueueUniquePeriodicWork(tagName
                , ExistingPeriodicWorkPolicy.KEEP
                , periodicWorkRequest);
    }

    public void scheduleJob(Context context){

        JobScheduler schedule = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder =
                new JobInfo.Builder(0, new ComponentName(context, LocationJob.class))

                        .setPeriodic(1000)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        ;//.setOverrideDeadline(3*60*1000) .setMinimumLatency(1)
        schedule.schedule(builder.build());

    }


    public static boolean checkPermission(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
