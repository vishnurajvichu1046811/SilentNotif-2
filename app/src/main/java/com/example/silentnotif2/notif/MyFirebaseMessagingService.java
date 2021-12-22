package com.example.silentnotif2.notif;


import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.silentnotif2.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject json = new JSONObject(params);
                Log.e(TAG, String.valueOf(json));
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(JSONObject data) {
        try {
            String title = data.getString("priority");
            String message = data.getString("bodyText");

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){

                        Intent intent3 = new Intent("ORDER");
                        sendBroadcast(intent3);
                    }else{

                        Intent intent3 = new Intent("ORDER");
                        sendBroadcast(intent3);
                    }

            if(data.has("is_silent_notification") && !data.getBoolean("is_silent_notification"))
            mNotificationManager.showSmallNotification(title, message, intent);


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppController.getInstance().setDeviceToken(s);
    }

}
