package capstoneproject.jatransit.FragmentHandler;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import capstoneproject.jatransit.Adapter.FeedListAdapter2;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.app.AppController;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class Nearby extends Fragment implements AdapterView.OnItemClickListener {//ListFragment {

    public static final String ARG_STRING = "Nearby";

    private static int REFRESH_TIME_IN_SECONDS = 5;
    private SwipeRefreshLayout swipeRefreshLayout;



    private static final String TAG = HomeScreen.class.getSimpleName();



    public View rootView;
    private ListView listView;
    private FeedListAdapter2 listAdapter;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;
    private String status;
    private TextView text;
    private TextView emptylist;

    private String URL_FEED = "http://jatransit.appspot.com/live";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        faActivity  = (FragmentActivity)    super.getActivity();
        rootView = inflater.inflate(R.layout.listview, container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter2(faActivity , feedItems);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);

        emptylist = (TextView)rootView.findViewById(R.id.message);
        //emptylist.setText("Internet Access is Needed to View live Bus Feed");

        //listView.setVisibility((listAdapter.isEmpty()) ? View.GONE : View.VISIBLE);

        rootView.setVisibility(android.view.View.VISIBLE);

        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

        try {
            Log.d("latitude","nothing");
            update();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        refresh();



        return rootView;

    }



    public void update() {

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());



                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);


    }

    //Retrieve the data from teh json feed
    private void parseJsonFeed(JSONObject response) {

        try {
            JSONArray feedArray = response.getJSONArray("trackedBus");


            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                item.setRoute("Route Number: " + feedObj.getString("route_id"));

                item.setOrigin("Origin: " + feedObj.getString("origin"));
                item.setVia("Via: " + feedObj.getString("via"));
                item.setDestination("Destination: " + feedObj.getString("destination"));

               // Log.d("route", feedObj.getString("route_id"));
                item.setVelocity(feedObj.getString("velocity"));
                //Log.d("velocity", feedObj.getString("velocity"));
                item.setLongitude(feedObj.getString("long"));
                //Log.d("long", feedObj.getString("long"));
                item.setLongitude(feedObj.getString("lat"));
               // Log.d("lat", feedObj.getString("lat"));



                //Calculate the currentlocation, distance and timestamp

                Location buslocation = new Location("");
                buslocation.setLatitude(Double.parseDouble(feedObj.getString("lat")));
                buslocation.setLongitude(Double.parseDouble(feedObj.getString("long")));

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //Log.d("buslocation",""+ location.getLatitude());
                double  c = location.distanceTo(buslocation);

                item.setDistance( "~ Distance: " + (int)(c/1000)+"km");

                //Log.d("distance", "" + c);
                double time = c/(Double.parseDouble(feedObj.getString("velocity"))*0.277778);

                item.setTimeStamp("Arrival Time: " + (int) time / 60 + "min");

                item.setCurrentlocation("Current Location: "+getAddress(Double.parseDouble(feedObj.getString("lat")), Double.parseDouble(feedObj.getString("long"))));

                //Log.d("timestamp", "" + time);

                feedItems.add(0, item);

            }

            listAdapter.notifyDataSetChanged();
            rootView.findViewById(R.id.pbHeaderProgress).setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       FeedItem item = feedItems.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("route", item.getRoute());
        bundle.putString("origin", item.getOrigin());
        bundle.putString("destination", item.getCurrentlocation());
        bundle.putString("distance",item.getDistance());
        bundle.putString("time",item.getTimeStamp());


        NearbyInfo info = NearbyInfo.newInstance(1, NearbyInfo.ARG_STRING);
        info.setArguments(bundle);
        FragmentManager fm4 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft4 =  fm4.beginTransaction();


        if (info.isAdded()){

            ft4.show(info);
        } else {

            ft4.replace(R.id.container, info, info.ARG_STRING);
        }

        ft4.addToBackStack(null);
        ft4.commit();


    }


    /**
     * The refresh function
     */
    private void refresh() {

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                feedItems = new ArrayList<FeedItem>();

                update();

                listAdapter = new FeedListAdapter2(getActivity(), feedItems);
                listView.setAdapter(listAdapter);


                Log.d(TAG, "onRefresh SwipeRefreshLayout");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopSwipeRefresh();


                    }
                }, REFRESH_TIME_IN_SECONDS * 1000);
            }

            private void stopSwipeRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        swipeRefreshLayout.setColorScheme(android.R.color.black,
                android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_green_light);





    }

    public String getAddress(double lat, double lng) {

        String add ="";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            add = obj.getFeatureName();

            Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return add;
    }


    public static Nearby newInstance(int someInt, String s) {

        Nearby nfragment = new Nearby();
        Bundle args = new Bundle();
        args.putInt("Nearby", someInt);
        nfragment.setArguments(args);
        return nfragment;
    }
}
