package com.umeox.livedata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public abstract class BaseReceiver extends BroadcastReceiver {

    //用于判断当前是否已经注册了
    private boolean isRegister = false;
    private static final String TAG = "BaseReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: action = " + intent.getAction());
        onAction(intent);
    }

    /**
     * 处理收到的广播信息
     * @param intent
     */
    protected abstract void onAction(Intent intent);

    protected void registerSelfNoData(Context context, String... actions) {
        registerSelf(context,null,actions);
    }

    protected void registerSelf(Context context, String dataScheme, String... actions) {
        if (!isRegister) {
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                filter.addAction(action);
            }
            if (dataScheme != null) {
                filter.addDataScheme(dataScheme);
            }

            isRegister = true;
            context.registerReceiver(this, filter);
        } else {
            Log.d(TAG, "registerSelf: already registerSelf");
        }
    }

    public abstract void register(Context context);

    public void unRegister(Context context) {
        if (isRegister) {
            context.unregisterReceiver(this);
        } else {
            Log.d(TAG, "unRegister: already unRegister");
        }
    }
}
