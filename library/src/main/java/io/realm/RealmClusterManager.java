package io.realm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Collection;

/**
 * Created by Reline on 11/13/16.
 * An implementation of the {@link ClusterManager} that handles processing an {@link OrderedRealmCollection}.
 */
public class RealmClusterManager<T extends RealmObject & ClusterItem> extends ClusterManager<T> {

    private OrderedRealmCollection<T> items;

    private RealmChangeListener listener = new RealmChangeListener() {
        @Override
        public void onChange(Object results) {
            getAlgorithm().clearItems();
            if (results instanceof OrderedRealmCollection) {
                Realm realm = Realm.getDefaultInstance();
                getAlgorithm().addItems(realm.copyFromRealm(items));
                realm.close();
            }
            cluster();
        }
    };

    public RealmClusterManager(Context context, GoogleMap map) {
        super(context, map);
    }

    public RealmClusterManager(Context context, GoogleMap map, MarkerManager markerManager) {
        super(context, map, markerManager);
    }

    @Override
    @Deprecated
    public void addItems(Collection<T> items) {
        throw new UnsupportedOperationException("This method is not supported by RealmClusterManager. Use updateRealmResults instead.");
    }

    @Override
    @Deprecated
    public void addItem(T myItem) {
        throw new UnsupportedOperationException("This method is not supported by RealmClusterManager. Use updateRealmResults instead.");
    }

    public void updateData(@Nullable OrderedRealmCollection<T> data) {
        if (data != null && !data.isManaged())
            throw new IllegalStateException("Only use this manager with managed list");

        if (items != null) {
            removeListener(items);
        }
        this.items = data;
        if (items != null) {
            addListener(items);
        }

        super.clearItems();
        Realm realm = Realm.getDefaultInstance();
        super.addItems(realm.copyFromRealm(items));
        realm.close();
        super.cluster();
    }

    private void addListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults realmResults = (RealmResults) data;
            //noinspection unchecked
            realmResults.addChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList realmList = (RealmList) data;
            //noinspection unchecked
            realmList.realm.handlerController.addChangeListenerAsWeakReference(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private void removeListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults realmResults = (RealmResults) data;
            realmResults.removeChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList realmList = (RealmList) data;
            //noinspection unchecked
            realmList.realm.handlerController.removeWeakChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }
}
