package com.troyanskiievgen.dmd.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.troyanskiievgen.dmd.R;
import com.troyanskiievgen.dmd.model.MapPoint;
import com.troyanskiievgen.dmd.network.RESTClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Relax on 24.07.2017.
 */

public class AppMapFragment extends MvpFragment implements GoogleMap.OnMarkerClickListener {

    @BindView(R.id.point_title)
    TextView pointTitle;
    @BindView(R.id.point_description)
    TextView pointDescription;
    @BindView(R.id.map_view)
    MapView mMapView;

    private GoogleMap googleMap;

    List<MapPoint> result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                setupMap();
            }
        });

        return view;
    }

    private boolean isLocationPermissionDisabled() {
        boolean isPermissionDisabled = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (isPermissionDisabled) {
            requestLocationPermission();
        }
        return isPermissionDisabled;
    }

    private void requestLocationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
        }
    }

    private void setupMap() {
        if (!isLocationPermissionDisabled()) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMarkerClickListener(this);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            RESTClient.getInstance().getMapPoints(2, new Callback<List<MapPoint>>() {
                @Override
                public void onResponse(Call<List<MapPoint>> call, Response<List<MapPoint>> response) {
                    result = response.body();
                    for(int i = 0; i < result.size(); i++) {
                        MapPoint point = result.get(i);
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(point.getLatLng())
                                .title(point.getTitle())
                                .snippet(point.getDescription()));
                        marker.setTag(i);
                    }
                }

                @Override
                public void onFailure(Call<List<MapPoint>> call, Throwable t) {
                    Log.d("DEBUG", "error when try to obtain data");
                }
            });


//            // For zooming automatically to the location of the marker
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            } else {
                // TODO: 24.07.2017 add info dialog
                requestLocationPermission();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        pointTitle.setText(result.get((int)marker.getTag()).getTitle());
        pointDescription.setText(result.get((int)marker.getTag()).getDescription());
        return true;
    }
}
