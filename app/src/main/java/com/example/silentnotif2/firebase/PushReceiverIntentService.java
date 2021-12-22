package com.example.silentnotif2.firebase;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PushReceiverIntentService extends IntentService {

    private final android.os.Handler.Callback callback = msg -> {
        throw new IllegalArgumentException("PUSH_RECEIVED NOT HANDLED!");
    };

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PushReceiverIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras.getParcelable("notification") != null) {
            sendNotification(extras.getParcelable("notification"));
        } else {

            String message = extras.getString("message");
            String url = extras.getString("url");
            com.example.silentnotif2.firebase.Notification notification = new com.example.silentnotif2.firebase.Notification(url, message);

            sendNotification(notification, extras);

            GcmBroadcastReceiver.completeWakefulIntent(intent);

        }
    }

    private void sendNotification(Notification notification) {
        sendNotification(notification, new Bundle());
    }

    private void sendNotification(com.example.silentnotif2.firebase.Notification notification, Bundle extras) {
        Intent broadcast = new Intent();
        //extras.putParcelable(Extras.NOTIFICATION, notification);
        broadcast.putExtras(extras);
        //broadcast.setAction(Events.BROADCAST_NOTIFICATION);

        sendOrderedBroadcast(broadcast, null, null, new Handler(callback), Activity.RESULT_OK, null, extras);
    }
}