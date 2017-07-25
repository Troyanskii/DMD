package com.troyanskiievgen.dmd.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Relax on 24.07.2017.
 */

public class NetworkReceiver extends BroadcastReceiver {

    NetworkStateReceiverListener listener;


    public NetworkReceiver(NetworkStateReceiverListener listener) {
        this.listener = listener;
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (isNetworkAvailable(context)) {
            listener.networkAvailable();
        } else {
            listener.networkUnavailable();
        }

    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public interface NetworkStateReceiverListener {
        void networkAvailable();


        void networkUnavailable();
    }
}