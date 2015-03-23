package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import capstoneproject.jatransit.MainActivity;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.app.AppController;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class Route extends ListFragment {

    public static final String ARG_STRING = "Route";


   /* private Cache cache;
    private Cache.Entry entry = null;

    private static final String TAG = HomeScreen.class.getSimpleName();

    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;
    public FeedListAdapter oldlistAdapter = null;
    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;
    private String status;


    private String URL_FEED ="http://test123calil.co.nf/monaspot/jatransit.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();


        rootView = inflater.inflate(R.layout.route, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity , feedItems);
        listView.setAdapter(listAdapter);

        rootView.setVisibility(android.view.View.VISIBLE);


        try {
            cache = AppController.getInstance().getRequestQueue().getCache();
            Calendar calendar = Calendar.getInstance();
            entry = cache.get(URL_FEED);
            long serverDate = entry.serverDate;

            if (entry != null) {
                if (getMinutesDifference(serverDate, calendar.getTimeInMillis()) >= 30) {

                    AppController.getInstance().getRequestQueue().getCache().invalidate(URL_FEED, true);
                    update();
                    status = "Updated";

                } else {
                    cache();
                    status = "No Updates";
                }
            } else {

                update();
                status = "Updated";
            }
        }catch (NullPointerException e){
            update();
            status = "Updated";
            e.printStackTrace();
        }

        return rootView;
    }

    public void update(){

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
    private void cache(){


        try {
            String data = new String(entry.data, "UTF-8");
            try {
                parseJsonFeed(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static long getMinutesDifference(long timeStart,long timeStop){
        long diff = timeStop - timeStart;
        long diffMinutes = diff / (60 * 1000);

        return  diffMinutes;
    }

    public void upButton(){
        listView.setSelection(0);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * *
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

                feedItems.add(0, item);

                System.out.println(item);
            }
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String[] values = new String[] { "24ex  Hellshire via Marcus Garvey Drive to City", "900  Hellshire via Hagley Pk Road to Half Way Tree", "75 Greater Portmore via Gregory Park to Spanish Town",
                "78 Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "68  Greater Portmore via Gregory Park to Spanish Town ", "50ex Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "75ex Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "500 Greater Portmore via Gregory Park to Spanish Town",
                "31 Hellshire via Hagley Pk Road to Half Way Tree", "32B Greater Portmore via Gregory Park to Spanish Town" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.route,R.id.route, values);
        setListAdapter(adapter);

    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }

    public static Route newInstance(int someInt, String s){

        Route rFragment = new Route();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        rFragment.setArguments(args);
        return rFragment;
    }
}
