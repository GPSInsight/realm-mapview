package com.gpsinsight.realmmapexample;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import io.realm.Realm;
import io.realm.RealmClusterManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private CoordinatorLayout coordinatorLayout;

    // keep a reference to the cluster manager in the activity scope
    // in order to listen for onChanges from the RealmChangeListener
    private RealmClusterManager<MyClusterItem> manager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        realm = Realm.getDefaultInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // add an item to realm where/when the map is clicked
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                realm.beginTransaction();
                realm.insert(new MyClusterItem(latLng.latitude, latLng.longitude));
                realm.commitTransaction();
            }
        });

        // create realm cluster manager and initialize data source
        manager = new RealmClusterManager<>(this, googleMap);
        manager.updateData(realm.where(MyClusterItem.class).findAll());

        // recluster pins when the map moves for a fluid feel
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                manager.cluster();
            }
        });

        // use the default cluster renderer to customize clustering options
        DefaultClusterRenderer<MyClusterItem> renderer = new DefaultClusterRenderer<>(this, googleMap, manager);
        renderer.setMinClusterSize(2);
        manager.setRenderer(renderer);

        // show snackbar when everything is ready
        Snackbar.make(coordinatorLayout, "Click anywhere to place a pin", Snackbar.LENGTH_INDEFINITE)
                .setAction("dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // no-op
                    }
                }).show();
    }
}
