package com.troyanskiievgen.dmd.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Relax on 24.07.2017.
 */

public class MapPoint {

    private long id;
    private String title;
    private String description;
    private double lat;
    private double lng;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
