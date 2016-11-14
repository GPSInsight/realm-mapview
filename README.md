## A Cluster Manager implementation for Realm

### Usage

```java
RealmClusterManager<MyClusterItem> manager = new RealmClusterManager<>(context, googleMap);
manager.updateData(realm.where(MyClusterItem.class).findAll());
```

Be sure to keep a reference to the `RealmClusterManager` while the `GoogleMap` is in use, otherwise you'll lose out on automatic updates.

Look at the example application or create an issue if you have any questions or improvements you'd like to add.

## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Included dependencies are:
Realm (https://github.com/realm/realm-java)
Google Maps Android API utility library (https://github.com/googlemaps/android-maps-utils)
```
