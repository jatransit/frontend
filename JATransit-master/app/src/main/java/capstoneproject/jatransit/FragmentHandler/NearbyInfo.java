package capstoneproject.jatransit.FragmentHandler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 05/08/2015.
 */
public class NearbyInfo extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private View rootView;
    private MapView mapView;
    private TextView distanceview, timestampview,routeview,originview,destinationview,locationview;


    String [] tempCoordinates = {"18.012061/-76.797698","18.011872/-76.797670","18.011908/-76.797488","18.011949/-76.797274","18.012005/-76.796909","18.020959/-76.770758","18.020296/-76.768194","18.019689/-76.765603","18.019413/-76.764063","18.019066/-76.762303","18.018602/-76.759997","18.017878/-76.756365","18.017495/-76.754225","18.017046/-76.751666","18.016663/-76.749906","18.016230/-76.747487","18.016097/-76.746618","18.015919/-76.745695","18.015633/-76.744118","18.015409/-76.743045","18.015743/-76.742391","18.016031/-76.741809","17.994998/-76.788781","18.015682/-76.741744","18.015307/-76.741916","18.015253/-76.742117"};


    public static final String ARG_STRING = "NearbyInfo";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.nearbyinfo, container,false);
        mapView = (MapView) rootView.findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);
        mMap = mapView.getMap();

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

        //Retrieving data from nearby route
        Bundle bundle = getArguments();

        String origin = bundle.getString("origin");
        Log.d("Tag", "this is: " + origin);
        String destine = bundle.getString("destination");
        String route = bundle.getString("route");
        String location = bundle.getString("location");
        String distance = bundle.getString("distance");
        String time = bundle.getString("time");

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


    private void makeUseOfNewLocation(Location location) {
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
        }*/

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
        mMap.animateCamera(cameraUpdate);
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


    /**
     *
     *Experiment
     */
/*

    public void displayClosestBus()
    {
        TrackedBus bus = findNearestBus();

        if(bus != null)
        {

            bus.getMarker().setTitle("Closest Bus!!");
            bus.getMarker().showInfoWindow();
            //displayToast("Closest bus found and displayed on map!");
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
                displayToast("Please enable GPS!!");
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



    public void simulateTracking()
    {

        for(TrackedBus bus: buses)
        {
            int locationIndex = bus.getCurrentLocationIndex();
            locationIndex++;
            int routeIndex = bus.getRouteIndex();

            if(locationIndex < routeList.get(routeIndex).size())
            {
                LatLng loc = (LatLng)routeList.get(routeIndex).get(locationIndex);
                bus.updateLocation(locationIndex, loc, interval);
                bus.getMarker().setPosition(loc);
                bus.getMarker().setTitle("Speed: " + df.format(bus.getVelocity()) + "km/h");

            }
            else
            {
                LatLng loc = (LatLng)routeList.get(routeIndex).get(0);
                bus.updateLocation(0, loc, interval);
                bus.getMarker().setPosition(loc);
            }

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
                Log.d("TAG", "drawRoute Error: " + e.toString());
            }
        }
        int color = Color.argb(255, rand.nextInt(256) + 128, rand.nextInt(256) + 128, rand.nextInt(256) + 128);

        LatLng location = route.get(0);

        int routeIndex = routeList.size();

        Marker m = mapFragment.getMap().addMarker( mOption.position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker)).anchor((float)0.5,(float)0.5));
        TrackedBus b = new TrackedBus(m,routeIndex,0,location);
        buses.add(b);

        pO.width(5).color(color);
        mapFragment.getMap().addPolyline(pO);

        routeList.add(route);

    }
    public void setupMap(GoogleMap map)
    {
        LatLng homeLoc = new LatLng(42.350, -71.146);
        LatLng jamhome = new LatLng(18.012,-76.797);

        map.setMyLocationEnabled(true);

        /*mOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker));
        mOption = new MarkerOptions();
        map.getUiSettings().setZoomControlsEnabled(true);

        timer = new Timer();

        if(mapType.compareTo("JA") == 0 )
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(jamhome, 13));
            new JaTransitFeedData().execute("http://server.jatransit.appspot.com/coordinates2");
        }
        else
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLoc, 10));

            timer.scheduleAtFixedRate( new TimerTask() {
                public void run() {
                    try
                    {
                        new MBTAFeedData().execute("http://developer.mbta.com/lib/GTRTFS/Alerts/VehiclePositions.pb");
                    }
                    catch (Exception e)
                    {
                        Log.d("TAG", "MBTAFeedData scheduleAtFixedRate Error: " + e.toString());
                    }
                }
            }, 0, 20000);
        }
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
         * the result from doInBackground()
        protected void onPostExecute(JSONArray routes) {

            try
            {
                for (int i = 0; i < routes.length(); i++) {
                    JSONObject jo = (JSONObject) routes.get(i);

                    String routeNum = jo.getString("route");
                    ArrayList<LatLng>  coordinates = new ArrayList<>();

                    String[] coordinateList = jo.getString("coordinates").split(",");

                    for (String coordinate : coordinateList) {

                        String[] coordinateArray = coordinate.split("/");
                        Log.d(TAG, "Lat: " + coordinateArray[0] + " Lon: " + coordinateArray[1]);
                        coordinates.add(new LatLng(Double.parseDouble(coordinateArray[0]), Double.parseDouble(coordinateArray[1])));
                    }

                    drawRoute(coordinates);
                }
            }
            catch (Exception e)
            {
                Log.d(TAG, "onPostExecute Error: " + e.toString());
            }

            setUpMapUpdater();

        }
    }


    private void setUpMapUpdater() {
        updater = new ScheduledThreadPoolExecutor(1);
        updater.scheduleAtFixedRate(new Runnable() {
            private Runnable update = new Runnable() {
                @Override
                public void run() {
                    simulateTracking();
                }
            };

            @Override
            public void run() {
                runOnUiThread(update);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private class AppLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            displayToast("Lat: " + loc.getLatitude() + " Log: " + loc.getLongitude() );
            setCurrentLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    protected class MBTAFeedData extends AsyncTask<String, Void, ArrayList<LatLng>> {

        protected ArrayList<LatLng> doInBackground(String... urls) {
            URL url;
            GtfsRealtime.FeedMessage feed;

            ArrayList<LatLng>  buses = new ArrayList<>();
            try
            {
                url = new URL(urls[0]);
                feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());
                int i = 0;
                for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                    //Log.d(TAG, "Loop");
                    if (entity.hasVehicle()) {
                        buses.add(new LatLng(entity.getVehicle().getPosition().getLatitude(),entity.getVehicle().getPosition().getLongitude()));
                    }
                }
            }
            catch (Exception e) {
                Log.d(TAG, "Error: " + e.toString());
            }

            return buses;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
        protected void onPostExecute(ArrayList<LatLng> locations) {

            clearMap();
            for(LatLng loc: locations)
            {
                try
                {
                    mapFragment.getMap().addMarker(mOption.position(loc));
                }
                catch(Exception e)
                {
                    Log.d(TAG, "Error: " + e.toString());
                }
            }


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

    }*/


    /*

     */

    public static NearbyInfo newInstance(int someInt, String s) {

        NearbyInfo nifragment = new NearbyInfo();
        Bundle args = new Bundle();
        args.putInt("NearbyInfo", someInt);
        nifragment.setArguments(args);
        return nifragment;
    }
}
