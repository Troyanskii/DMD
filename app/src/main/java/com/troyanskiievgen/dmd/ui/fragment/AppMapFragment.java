package com.troyanskiievgen.dmd.ui.fragment;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.troyanskiievgen.dmd.R;
import com.troyanskiievgen.dmd.model.MapPoint;
import com.troyanskiievgen.dmd.network.NetworkReceiver;
import com.troyanskiievgen.dmd.presenter.AppMapFragmentPresenter;
import com.troyanskiievgen.dmd.view.AppMapFragmentView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Relax on 24.07.2017.
 */

public class AppMapFragment extends MvpFragment implements AppMapFragmentView, GoogleMap.OnMarkerClickListener, NetworkReceiver.NetworkStateReceiverListener {

    @BindView(R.id.point_title)
    TextView pointTitle;
    @BindView(R.id.point_description)
    TextView pointDescription;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPanel;

    @InjectPresenter(type = PresenterType.GLOBAL)
    AppMapFragmentPresenter mapFragmentPresenter;

    private GoogleMap googleMap;

    private NetworkReceiver networkStateReceiver;

    public static AppMapFragment getInstance() {
        // TODO: 24.07.2017 you could put some data in bundle here if you need to store some data
        return new AppMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        initMapView(savedInstanceState);
        setupSlidePanel();
        mapFragmentPresenter.hidePanel();
        return view;
    }

    private void initMapView(Bundle savedInstanceState) {
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
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mapFragmentPresenter.setupData();
        }
    }

    private void setupSlidePanel() {
        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO: 25.07.2017 we could use offset to set panel proportional in same position when change oriintation
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    mapFragmentPresenter.expandPanel();
                }
                if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    mapFragmentPresenter.collapsePanel();
                }
            }
        });
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
        registerNetworkListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterNetworkListener();
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
        mapFragmentPresenter.onMarkerClick((int)marker.getTag());
        return true;
    }


    public void registerNetworkListener() {
        if (networkStateReceiver == null) {
            networkStateReceiver = new NetworkReceiver(this);
            getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


    public void unregisterNetworkListener() {
        if (networkStateReceiver != null) {
            getActivity().unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }
    }

    @Override
    public void networkAvailable() {
        setupMap();
    }

    @Override
    public void networkUnavailable() {
        if (getActivity() != null) {
            // TODO: 25.07.2017 we could transfer check network connection in activity when we have more then 1 fragment
            Toast.makeText(getActivity(), R.string.error_internen_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addMarkerToMap(MapPoint point, int position) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(point.getLatLng())
                .title(point.getTitle())
                .snippet(point.getDescription()));
        marker.setTag(position);
    }

    @Override
    public void showError(String error) {
        // TODO: 24.07.2017 impl show some error
    }

    @Override
    public void onMarkerClick(MapPoint mapPoint) {
        pointTitle.setText(mapPoint.getTitle());
        pointDescription.setText(mapPoint.getDescription());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mapPoint.getLatLng()).zoom(2).build();
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mapFragmentPresenter.collapsePanel();
    }

    @Override
    public void collapsePanel() {
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void expandPanel() {
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void hidePanel() {
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }
}
