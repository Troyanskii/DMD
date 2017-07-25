package com.troyanskiievgen.dmd.presenter;

import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.troyanskiievgen.dmd.R;
import com.troyanskiievgen.dmd.model.MapPoint;
import com.troyanskiievgen.dmd.network.RESTClient;
import com.troyanskiievgen.dmd.view.AppMapFragmentView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Relax on 24.07.2017.
 */

@InjectViewState
public class AppMapFragmentPresenter extends MvpPresenter<AppMapFragmentView>{

    private List<MapPoint> pointsResult;
    private boolean isPointSelected = false;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void setupData() {
        if (pointsResult == null) {
            uploadPoints();
        } else {
            drawResultPoints(pointsResult);
        }
    }

    private void uploadPoints() {
        RESTClient.getInstance().getMapPoints(2, new Callback<List<MapPoint>>() {
            @Override
            public void onResponse(Call<List<MapPoint>> call, Response<List<MapPoint>> response) {
                pointsResult = response.body();
                if (pointsResult != null) {
                    drawResultPoints(pointsResult);
                }
            }

            @Override
            public void onFailure(Call<List<MapPoint>> call, Throwable t) {
                getViewState().showError("Error");
            }
        });
    }

    private void drawResultPoints(List<MapPoint> pointsResult) {
        for(int i = 0; i < pointsResult.size(); i++) {
            getViewState().addMarkerToMap(pointsResult.get(i), i);
        }
    }

    public void onMarkerClick(int position) {
        getViewState().onMarkerClick(pointsResult.get(position));
        isPointSelected = true;
    }

    public void collapsePanel() {
        getViewState().collapsePanel();
    }

    public void expandPanel() {
        getViewState().expandPanel();
    }

    public void hidePanel() {
        if(!isPointSelected) {
            getViewState().hidePanel();
        }
    }
}
