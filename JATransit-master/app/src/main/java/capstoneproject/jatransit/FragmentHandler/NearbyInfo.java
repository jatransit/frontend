package capstoneproject.jatransit.FragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 05/08/2015.
 */
public class NearbyInfo extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private View rootView;
    private MapView mapView;

    public static final String ARG_STRING = "NearbyInfo";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.nearbyinfo, container,false);
        mapView = (MapView) rootView.findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);
        mMap = mapView.getMap();
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
               // makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return rootView;
    }

    public static NearbyInfo newInstance(int someInt, String s) {

        NearbyInfo nifragment = new NearbyInfo();
        Bundle args = new Bundle();
        args.putInt("NearbyInfo", someInt);
        nifragment.setArguments(args);
        return nifragment;
    }
}
