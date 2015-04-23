package capstoneproject.jatransit.FragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;

import capstoneproject.jatransit.R;

public class MapsFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public static final String ARG_STRING= "Map";
    View rootView;
    MapView mapView;

    String [] tempCoordinates = {"18.012061/-76.797698","18.011872/-76.797670","18.011908/-76.797488","18.011949/-76.797274","18.012005/-76.796909","18.020959/-76.770758","18.020296/-76.768194","18.019689/-76.765603","18.019413/-76.764063","18.019066/-76.762303","18.018602/-76.759997","18.017878/-76.756365","18.017495/-76.754225","18.017046/-76.751666","18.016663/-76.749906","18.016230/-76.747487","18.016097/-76.746618","18.015919/-76.745695","18.015633/-76.744118","18.015409/-76.743045","18.015743/-76.742391","18.016031/-76.741809","17.994998/-76.788781","18.015682/-76.741744","18.015307/-76.741916","18.015253/-76.742117"};


    public MapsFragment() {
        // Empty constructor required for fragment subclasses
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_maps,container,false);
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
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        return rootView;

    }

    private void makeUseOfNewLocation(Location location) {
        try {

            Toast.makeText(getActivity(), location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();

            ArrayList<Double> temp = new ArrayList<Double>();
            for (int i = 0; i < tempCoordinates.length; i++) {

               // double c = Math.sqrt(Math.pow(Math.abs(Double.parseDouble(tempCoordinates[i].split("/")[0])) - Math.abs(location.getLatitude()), 2) + Math.pow(Math.abs(Double.parseDouble(tempCoordinates[i].split("/")[1])) - Math.abs(location.getLongitude()), 2));
                Location busStopscord = new Location("");
                busStopscord.setLatitude(Double.parseDouble(tempCoordinates[i].split("/")[0]));
                busStopscord.setLongitude(Double.parseDouble(tempCoordinates[i].split("/")[1]));

                 double  c= location.distanceTo(busStopscord);
                temp.add(c);
                Log.d("distance",""+c);


            }
            double d = Collections.min(temp);
            int e = temp.indexOf(d);
            Toast.makeText(getActivity(), tempCoordinates[e].split("/")[0] + " " + tempCoordinates[e].split("/")[1], Toast.LENGTH_SHORT).show();
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(tempCoordinates[e].split("/")[0]), Double.parseDouble(tempCoordinates[e].split("/")[1])))
                    .title("Bus Stop"));

            LatLng sourcePosition = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng destPosition = new LatLng(Double.parseDouble(tempCoordinates[e].split("/")[0]), Double.parseDouble(tempCoordinates[e].split("/")[1]));
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception error) {
            error.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
        mMap.animateCamera(cameraUpdate);
    }


    public void getLocation(GoogleMap map){

        Location myLocation =  map.getMyLocation();
        Toast.makeText(getActivity(), "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        if(myLocation!=null)
        {
            double dLatitude = myLocation.getLatitude();
            double dLongitude = myLocation.getLongitude();
            Log.w("APPLICATION", " : " + dLatitude);
            Log.w("APPLICATION"," : "+dLongitude);
            map.addMarker(new MarkerOptions().position(
                    new LatLng(dLatitude, dLongitude)).title("My Location"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));

        }
        else
        {
            Toast.makeText(getActivity(), "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }

    }

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Needs to be called before setting the content view
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_maps);



        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

        //?android:attr/actionBarSize
        int actionBarSize = obtainActionBarHeight();

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private int obtainActionBarHeight() {
        int[] textSizeAttr = new int[] { android.R.attr.actionBarSize };
        TypedValue typedValue = new TypedValue();
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int textSize = a.getDimensionPixelSize(0, -1);
        a.recycle();

        return textSize;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public static MapsFragment newInstance(int someInt, String s) {
        MapsFragment mfragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        mfragment.setArguments(args);
        return mfragment;
    }





}
