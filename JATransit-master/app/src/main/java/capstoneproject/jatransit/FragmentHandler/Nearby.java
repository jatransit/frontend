package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class Nearby extends Fragment{//ListFragment {

    public static final String ARG_STRING = "Nearby";

    private static int REFRESH_TIME_IN_SECONDS = 5;
    //public SwipeRefreshLayout swipeRefreshLayout;

    private Cache cache;
    private Cache.Entry entry = null;

    private static final String TAG = HomeScreen.class.getSimpleName();



    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;
    private String status;

    private String URL_FEED = "http://test123calil.co.nf/monaspot/jatransit.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        faActivity  = (FragmentActivity)    super.getActivity();
        rootView = inflater.inflate(R.layout.listview, container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity , feedItems);
        listView.setAdapter(listAdapter);

        rootView.setVisibility(android.view.View.VISIBLE);

        try {
            cache = AppController.getInstance().getRequestQueue().getCache();
            AppController.getInstance().getRequestQueue().getCache().invalidate(URL_FEED, true);
            update();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

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
                item.setTimeStamp(feedObj.getString("timeStamp"));

                feedItems.add(0,item);

            }

            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

 /*   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String[] values = new String[] { "24ex  Hellshire via Marcus Garvey Drive to City", "900  Hellshire via Hagley Pk Road to Half Way Tree", "75 Greater Portmore via Gregory Park to Spanish Town",
                "78 Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "68  Greater Portmore via Gregory Park to Spanish Town ", "50ex Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "75ex Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "500 Greater Portmore via Gregory Park to Spanish Town",
                "31 Hellshire via Hagley Pk Road to Half Way Tree", "32B Greater Portmore via Gregory Park to Spanish Town" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.nearby,R.id.route, values);
        setListAdapter(adapter);

    }*/
    public static Nearby newInstance(int someInt, String s) {

       Nearby nfragment = new Nearby();
        Bundle args = new Bundle();
        args.putInt("homescreen", someInt);
        nfragment.setArguments(args);
        return nfragment;
    }
}
