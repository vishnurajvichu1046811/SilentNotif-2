package com.example.silentnotif2.notif;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.silentnotif2.MainActivity;
import com.example.silentnotif2.R;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private SharedPreferences sharedPref;
    static int notifCount = 0;
    public static Boolean isConnected(final Activity activity) {
        Boolean check = false;
        ConnectivityManager ConnectionManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            check = true;
        } else {
            Toast.makeText(activity, activity.getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(myReceiver, new IntentFilter("ORDER"));
        mInstance = this;
        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
      /*  Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );*/

    }

    public String getDeviceToken() {
        return sharedPref.getString("DEVICETOKEN", "");
    }

    public void setDeviceToken(String token) {
        sharedPref.edit().putString("DEVICETOKEN", token).apply();
    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("Broadcast : ","Notification Received"+" count = "+notifCount);
            notifCount = notifCount +1;
            if(intent.getAction().equals("ORDER")) {
                String id = intent.getStringExtra("order_id");
                try {
                    Intent intent2 = new Intent(mInstance, MainActivity.class);
                    intent2.putExtra("order_id",id);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    };


}
