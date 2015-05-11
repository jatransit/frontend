package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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



 /*   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        String[] values = new String[] { "24ex  Hellshire via Marcus Garvey Drive to City", "900  Hellshire via Hagley Pk Road to Half Way Tree", "75 Greater Portmore via Gregory Park to Spanish Town",
                "78 Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "68  Greater Portmore via Gregory Park to Spanish Town ", "50ex Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "75ex Christian Gardens via  Gregory Pk.Hagley Pk Road to Half Way Tree", "500 Greater Portmore via Gregory Park to Spanish Town",
                "31 Hellshire via Hagley Pk Road to Half Way Tree", "32B Greater Portmore via Gregory Park to Spanish Town" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.settings.nearby,R.id.route, values);
        setListAdapter(adapter);

    }*/
    public static Nearby newInstance(int someInt, String s) {

       Nearby nfragment = new Nearby();
        Bundle args = new Bundle();
        args.putInt("Nearby", someInt);
        nfragment.setArguments(args);
        return nfragment;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MapsFragment map = MapsFragment.newInstance(1,MapsFragment.ARG_STRING);
        FragmentManager fm3 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft3 = fm3.beginTransaction();

        NearbyInfo info = NearbyInfo.newInstance(1, NearbyInfo.ARG_STRING);
        FragmentManager fm4 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft4 =  fm4.beginTransaction();


        if (info.isAdded()){//(map.isAdded()) {
           // ft3.show(map);
            ft4.show(info);
        } else {
           // ft3.replace(R.id.container, map, map.ARG_STRING);
            ft4.replace(R.id.container, info, info.ARG_STRING);
        }
       // ft3.addToBackStack(null);
            ft4.addToBackStack(null);
            ft4.commit();
        //ft3.commit();

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

            case R.id.settings:

                return true;

            case R.id.action_search:



                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
