package capstoneproject.jatransit.FragmentHandler;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Caliph Cole on 05/11/2015.
 */
public class Search extends Fragment{

    public static final String ARG_STRING = "Search";



    private static final String TAG = HomeScreen.class.getSimpleName();

    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;
    private LinearLayout linlaHeaderProgress;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;


    public DBHelper routedb;

    private TextView text;

    private String URL_FEED ="http://jatransit.appspot.com/routes";

    private TextView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        routedb = new DBHelper(getActivity());
        faActivity  = (FragmentActivity)    super.getActivity();

        rootView = inflater.inflate(R.layout.listview, container,false);

        LinearLayout linlaHeaderProgress = (LinearLayout)rootView.findViewById(R.id.progress);


        message = (TextView) rootView.findViewById(R.id.message);
        message.setText("Quick Search For Bus Routes");
        message.setTextColor(getResources().getColor(R.color.quicksearch));
        listView = (ListView) rootView.findViewById(R.id.listView);


        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity ,feedItems);


        faActivity.setProgressBarIndeterminateVisibility(true);

        listView.setAdapter(listAdapter);


        rootView.setVisibility(android.view.View.VISIBLE);

        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

        rootView.findViewById(R.id.pbHeaderProgress).setVisibility(View.GONE);

        return rootView;

    }

    public FeedListAdapter getListAdapter(){

        return listAdapter;
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


                if(routedb.numberOfRows()>0) {
                    List<FeedItem> temp = routedb.getAllRoutes();
                    for (int i = 0; i < temp.size(); i++) {
                        feedItems.add(0, temp.get(i));
                    }

                    Log.d("Tag", "" + feedItems.size());


                    listAdapter.notifyDataSetChanged();
                }else{

                    Toast.makeText(getActivity(), "There are no routes locally stored", Toast.LENGTH_LONG).show();
                }
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

                if(feedArray.length()> routedb.numberOfRows()){

                    routedb.insertRoute(feedObj.getString("route"), feedObj.getString("origin"),feedObj.getString("destination"),feedObj.getString("via"), feedObj.getString("route_type"));

                }

            }




            List<FeedItem> temp = routedb.getAllRoutes();
            for(int i = 0;i< temp.size(); i++) {
                feedItems.add(0, temp.get(i));
            }

           /* String s [] = feedItems.get(0).getOrigin().split(":");
            SpannableString spanString = new SpannableString(feedItems.get(0).getOrigin().split(":")[0]);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            feedItems.get(0).getOrigin().split(":")[0]= spanString.toString();

            feedItems.get(0).setOrigin(spanString+ s[1]);*/




            listAdapter.notifyDataSetChanged();

            rootView.findViewById(R.id.progress).setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFragment(){
        return ARG_STRING;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));

        searchView.setQueryHint("Enter Route#");
        searchView.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {
                listAdapter.getOriginalfeedItems();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            DBHelper db = new DBHelper(getActivity());
            @Override
            public boolean onQueryTextSubmit(String s) {

                rootView.findViewById(R.id.pbHeaderProgress).setVisibility(View.VISIBLE);
                message.setText("");
                feedItems.clear();
                List<FeedItem> temp = db.getDataByRoute(s);
                for(int i = 0;i< temp.size(); i++) {
                    feedItems.add(0, temp.get(i));

                }
                if(feedItems.size()>0){
                    message.setText("");
                }
                listAdapter.notifyDataSetChanged();
                rootView.findViewById(R.id.pbHeaderProgress).setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               /* if(listAdapter.getOriginalListCount()>0){
                    listAdapter.getFilter().filter(newText);
                }*/

                return false;
            }


        });

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

    public static Search newInstance(int someInt, String s){

        Search sFragment = new Search();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        sFragment.setArguments(args);
        return sFragment;
    }
}
