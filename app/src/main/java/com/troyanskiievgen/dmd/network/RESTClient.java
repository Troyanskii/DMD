package com.troyanskiievgen.dmd.network;

import com.troyanskiievgen.dmd.model.MapPoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Relax on 24.07.2017.
 */

public class RESTClient {

    private static RESTClient instance;

    private RESTClient() {
    }

    public static RESTClient getInstance() {
        if (instance == null) {
            instance = new RESTClient();
        }
        return instance;
    }

    public void getMapPoints(int intend, Callback<List<MapPoint>> callback) {
        NetworkManager.getRESTService().getMapPoints(intend).enqueue(callback);
    }
}
