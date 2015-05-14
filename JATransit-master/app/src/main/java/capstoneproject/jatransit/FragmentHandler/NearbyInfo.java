package capstoneproject.jatransit.FragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import capstoneproject.jatransit.R;
import capstoneproject.jatransit.nearbyhandler.TrackedBus;

/**
 * Created by Caliph Cole on 05/08/2015.
 */
public class NearbyInfo extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static View rootView;
    private MapView mapView;
    private TextView distanceview, timestampview,routeview,originview,destinationview,locationview;
    LocationManager locationManager;
    private MapFragment mapFragment;
    final String TAG = "JaTransit";
    final String mapType = "JA";
    protected LatLng currentLocation;
    MarkerOptions mOption;
    Random rand = new Random();
    Timer timer;

    String origin;

    String destine;
    String route ;
    String location ;
    String distance;
    String time ;


    private ArrayList<ArrayList> routeList = new ArrayList<>();
    private ArrayList<TrackedBus> buses = new ArrayList<>();



    public static final String ARG_STRING = "NearbyInfo";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {

            rootView = inflater.inflate(R.layout.nearbyinfo, container, false);


        }catch (InflateException e) {
    /* map is already there, just return view as it is */
        }


        getActivity().findViewById(R.id.help).setVisibility(View.GONE);
        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationListener locationListener = new AppLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);



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
         locationListener = new LocationListener() {
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

        //Retrieving data from nearby route
        Bundle bundle = getArguments();

        origin = bundle.getString("origin");

        destine = bundle.getString("destination");
        route = bundle.getString("route");
        location = bundle.getString("location");
        distance = bundle.getString("distance");
        time = bundle.getString("time");

        busDetails(origin,destine, route, location,distance,time);



        return rootView;
    }

    private void busDetails(String origin,String destine, String route, String location, String distance, String time){


        originview = (TextView) rootView.findViewById(R.id.org);
        originview.setText(origin);


        destinationview = (TextView) rootView.findViewById(R.id.des);
        destinationview.setText(destine);


        locationview = (TextView)rootView.findViewById(R.id.location);
        locationview.setText(location);


        distanceview = (TextView) rootView.findViewById(R.id.distance);
        distanceview.setText(distance);


        timestampview = (TextView) rootView.findViewById(R.id.timestamp);
        timestampview.setText(time);


        routeview =(TextView) rootView.findViewById(R.id.route);
        routeview.setText(route);


    }


   /* private void makeUseOfNewLocation(Location location) {
       /* try {

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
        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
       // mMap.animateCamera(cameraUpdate);
    }*/



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                getActivity().finish();
                return true;

            case R.id.action_search:



                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        setupMap(map);
    }


    public void simulateTracking(JSONArray livebuses)
    {
        try
        {
            for (int i = 0; i < livebuses.length(); i++) {
                JSONObject jo = (JSONObject) livebuses.get(i);

                String lat = jo.getString("lat");
                String lon = jo.getString("long");
                String origin = jo.getString("origin");
                String via = jo.getString("via");
                String destination = jo.getString("destination");
                String velocity = jo.getString("velocity");
                String bus_id = jo.getString("bus_id");
                String route_id = jo.getString("route_id");
                String direction = jo.getString("direction");

                LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

                boolean found = false;
                for(TrackedBus bus: buses)
                {
                    if(bus.getBusId().equals(bus_id))
                    {
                        String s [] = route.split(": ");
                        if(route_id.equals(s[1])) {

                            found = true;
                            bus.setVelocity(Double.parseDouble(velocity));
                            bus.getMarker().setTitle("Route#: " + route_id);
                            bus.setCurrentLocation(location);
                            bus.getMarker().setPosition(location);
                        }
                    }
                }

                if(!found)
                {
                    String s [] = route.split(": ");
                    if(route_id.equals(s[1])) {
                        Marker m = mapFragment.getMap().addMarker(mOption.position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker)).anchor((float) 0.5, (float) 0.5));
                        TrackedBus b = new TrackedBus(bus_id, m, 0, location);
                        b.setVelocity(Double.parseDouble(velocity));
                        b.getMarker().setTitle("Route#: " + route_id);
                        buses.add(b);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "onPostExecute Error: " + e.toString());
        }
    }

    public void clearMap()
    {
        try
        {
            mapFragment.getMap().clear();
        }
        catch(Exception e)
        {
            Log.d(TAG, "Error: " + e.toString());
        }

    }

    public void drawRoute(ArrayList<LatLng> route)
    {
        PolylineOptions pO = new PolylineOptions();

        for(LatLng loc: route)
        {
            try
            {
                pO.add(loc);
            }
            catch(Exception e)
            {
                Log.d(TAG, "drawRoute Error: " + e.toString());
            }
        }
        int color = Color.argb(255, rand.nextInt(256) + 128, rand.nextInt(256) + 128, rand.nextInt(256) + 128);

        pO.width(5).color(color);
        mapFragment.getMap().addPolyline(pO);
        routeList.add(route);

    }

    public void setupMap(GoogleMap map)
    {
        //LatLng homeLoc = new LatLng(42.350, -71.146);
        LatLng jamhome = new LatLng(18.012,-76.797);

        map.setMyLocationEnabled(true);

        /*mOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker));*/
        mOption = new MarkerOptions();
        map.getUiSettings().setZoomControlsEnabled(true);

        clearMap();

        timer = new Timer();

        if(mapType.compareTo("JA") == 0 )
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(jamhome, 13));
            new JaTransitFeedData().execute("http://server.jatransit.appspot.com/coordinates2");

            timer.scheduleAtFixedRate( new TimerTask() {
                public void run() {
                    try
                    {
                        //new JATransitLiveFeed().execute("http://developer.mbta.com/lib/GTRTFS/Alerts/VehiclePositions.pb");
                        new JATransitLiveFeed().execute("http://jatransit.appspot.com/live");
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "JATransitLiveFeed scheduleAtFixedRate Error: " + e.toString());
                    }
                }
            }, 0, 20000);
        }
    }


    public void displayClosestBus()
    {
        TrackedBus bus = findNearestBus();

        if(bus != null)
        {

            bus.getMarker().setTitle("Closest Bus!!");
            bus.getMarker().showInfoWindow();

        }
    }
    public TrackedBus findNearestBus()
    {
        TrackedBus closest = null;
        double shortestDistance = 999999999;
        double distance = 0;

        if(getCurrentLocation() == null)
        {
            String locationProvider = LocationManager.NETWORK_PROVIDER; // Or use LocationManager.GPS_PROVIDER
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            setCurrentLocation(lastKnownLocation);

            if(getCurrentLocation() == null)
            {

                return null;
            }
        }


        for(TrackedBus bus: buses)
        {
            distance = calculateDistanceInMeters(getCurrentLocation(),bus.getLocation());

            if( distance < shortestDistance )
            {
                closest = bus;
                shortestDistance = distance;
            }
        }

        return  closest;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation)
    {
        this.currentLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
    }


    protected class JaTransitFeedData extends AsyncTask<String, Void, JSONArray> {

        protected JSONArray doInBackground(String... urls) {
            URL url;
            JSONArray routes = new JSONArray();
            try
            {
                url = new URL(urls[0]);

                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                url.openStream()));
                String next;
                while ((next = bufferedReader.readLine()) != null){
                    JSONObject ja = new JSONObject(next);

                    routes = ja.getJSONArray("routes");
                }
            }
            catch (Exception e) {
                Log.d(TAG, "doInBackground Error: " + e.toString());
            }
            return routes;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(JSONArray routes) {

            try
            {
                for (int i = 0; i < routes.length(); i++) {
                    JSONObject jo = (JSONObject) routes.get(i);

                    String routeNum = jo.getString("route");
                    ArrayList<LatLng>  coordinates = new ArrayList<>();

                    String[] coordinateList = jo.getString("coordinates").split(",");

                    for (String coordinate : coordinateList) {
                        Log.d("Testing routnum",route +" "+ routeNum);
                        String s [] = route.split(": ");
                        if(routeNum.equals(s[1])) {
                            String[] coordinateArray = coordinate.split("/");
                            Log.d(TAG, "Lat: " + coordinateArray[0] + " Lon: " + coordinateArray[1]);
                            coordinates.add(new LatLng(Double.parseDouble(coordinateArray[0]), Double.parseDouble(coordinateArray[1])));
                        }
                    }

                        drawRoute(coordinates);
                }
            }
            catch (Exception e)
            {
                Log.d(TAG, "onPostExecute Error: " + e.toString());
            }

        }
    }


    private class AppLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {


            setCurrentLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    protected class JATransitLiveFeed extends AsyncTask<String, Void, JSONArray> {

        protected JSONArray doInBackground(String... urls) {
            URL url;
            JSONArray buses = new JSONArray();
            try
            {
                url = new URL(urls[0]);

                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                url.openStream()));
                String next;
                while ((next = bufferedReader.readLine()) != null){
                    JSONObject ja = new JSONObject(next);

                    buses = ja.getJSONArray("trackedBus");
                }
            }
            catch (Exception e) {
                Log.d(TAG, "doInBackground Error: " + e.toString());
            }
            return buses;
        }


        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(JSONArray buses) {

            simulateTracking(buses);
        }
    }

    public double calculateDistanceInMeters(LatLng loc1, LatLng loc2)
    {
        double lon1 = loc1.longitude;
        double lat1 = loc1.latitude;
        double lon2 = loc2.longitude;
        double lat2 = loc2.latitude;

        double x = Math.toRadians(lon1 - lon2) * Math.cos( Math.toRadians( lat1 ) );
        double y = Math.toRadians(lat1 - lat2);

        return  6371000.0 * Math.sqrt( x*x + y*y );

    }




    public static NearbyInfo newInstance(int someInt, String s) {

        NearbyInfo nifragment = new NearbyInfo();
        Bundle args = new Bundle();
        args.putInt("NearbyInfo", someInt);
        nifragment.setArguments(args);
        return nifragment;
    }
}
