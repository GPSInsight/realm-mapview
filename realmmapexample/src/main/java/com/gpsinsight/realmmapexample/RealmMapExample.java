package com.gpsinsight.realmmapexample;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmMapExample extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build()
        );
    }
}
