package capstoneproject.jatransit.FragmentHandler;

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

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import capstoneproject.jatransit.Adapter.FeedListAdapter;
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

    private Cache cache;
    private Cache.Entry entry = null;

    private static final String TAG = HomeScreen.class.getSimpleName();



    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;
    private String status;
    private TextView text;

    private String URL_FEED = "http://test123calil.co.nf/monaspot/jatransit.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        faActivity  = (FragmentActivity)    super.getActivity();
        rootView = inflater.inflate(R.layout.listview, container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity , feedItems);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);

        rootView.setVisibility(android.view.View.VISIBLE);

        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

        try {
            cache = AppController.getInstance().getRequestQueue().getCache();
            AppController.getInstance().getRequestQueue().getCache().invalidate(URL_FEED, true);
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
            JSONArray feedArray = response.getJSONArray("route");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                item.setRoute(feedObj.getString("route"));

                item.setOrigin(feedObj.getString("origin"));
                item.setVia(feedObj.getString("via"));
                item.setDestination(feedObj.getString("destination"));
               // item.setRouteType(feedObj.getString("route_type"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                feedItems.add(0,item);

            }

            listAdapter.notifyDataSetChanged();
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
        bundle.putString("destination",item.getDestination());
        bundle.putString("location","current Location");
        bundle.putString("distance","3km");
        bundle.putString("time","15min");


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

                listAdapter = new FeedListAdapter(getActivity(), feedItems);
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




    public static Nearby newInstance(int someInt, String s) {

        Nearby nfragment = new Nearby();
        Bundle args = new Bundle();
        args.putInt("Nearby", someInt);
        nfragment.setArguments(args);
        return nfragment;
    }
}
