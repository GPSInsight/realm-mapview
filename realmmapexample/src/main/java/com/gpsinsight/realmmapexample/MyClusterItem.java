package com.gpsinsight.realmmapexample;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.realm.RealmObject;

public class MyClusterItem extends RealmObject implements ClusterItem {

    private double lat;
    private double lng;

    public MyClusterItem() {
        // default constructor
    }

    public MyClusterItem(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }
}
