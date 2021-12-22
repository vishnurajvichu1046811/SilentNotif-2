package com.example.silentnotif2.firebase;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent push = new Intent(context, PushReceiverIntentService.class);
        push.putExtras(getResultExtras(true));
        context.startService(push);
        setResultCode(Activity.RESULT_OK);
    }
}