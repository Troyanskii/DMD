package com.troyanskiievgen.dmd.network;

import com.troyanskiievgen.dmd.model.MapPoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Relax on 24.07.2017.
 */

interface RESTService {

    @GET(ConstantsAPI.GET_MAP_POINTS)
    Call<List<MapPoint>> getMapPoints(@Query("indent") int indent);
}
