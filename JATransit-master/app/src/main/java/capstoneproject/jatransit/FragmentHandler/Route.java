package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import capstoneproject.jatransit.data.DBHelper;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class Route extends Fragment {

    public static final String ARG_STRING = "Route";



    private Cache cache;
    private Cache.Entry entry = null;

    private static final String TAG = HomeScreen.class.getSimpleName();

    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;


    public DBHelper routedb;


    private String URL_FEED ="http://jatransit.appspot.com/routes";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        routedb = new DBHelper(getActivity());
       faActivity  = (FragmentActivity)    super.getActivity();
        rootView = inflater.inflate(R.layout.listview, container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);

       feedItems = new ArrayList<FeedItem>();

       listAdapter = new FeedListAdapter(faActivity ,feedItems);

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




    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("routes");

            routedb = new DBHelper(getActivity());

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();

               /* item.setOrigin(feedObj.getString("origin"));
                item.setDestination(feedObj.getString("destination"));
                item.setRoute(feedObj.getString("route"));
                item.setVia(feedObj.getString("via"));

                item.setRouteType(feedObj.getString("route_type"));

                    */

                //Insert in the sqlite database what is on the server
               routedb.insertRoute(feedObj.getString("route"), feedObj.getString("origin"),feedObj.getString("destination"),feedObj.getString("via"), feedObj.getString("route_type"));




               // feedItems.add(0, item);






                }


            List<FeedItem> temp = routedb.getAllRoutes();
            for(int i = 0;i< temp.size();i++) {
                feedItems.add(0, temp.get(i));
            }

            Log.d("Tag",""+ feedItems.size());



           listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFragment(){
        return ARG_STRING;
    }
   /* @Override
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
    }*/

    public static Route newInstance(int someInt, String s){

        Route rFragment = new Route();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        rFragment.setArguments(args);
        return rFragment;
    }
}
