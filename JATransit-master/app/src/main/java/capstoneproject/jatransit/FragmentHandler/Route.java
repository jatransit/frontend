package capstoneproject.jatransit.FragmentHandler;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Created by Caliph Cole on 03/05/2015.
 */
public class Route extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_STRING = "Route";



    private static final String TAG = HomeScreen.class.getSimpleName();

    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;
    private LinearLayout linlaHeaderProgress;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;


    public DBHelper routedb;
    TimeTable timetable;

    private TextView text;

    private String URL_FEED ="http://jatransit.appspot.com/routes";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        routedb = new DBHelper(getActivity());
        faActivity  = (FragmentActivity)    super.getActivity();

       // faActivity.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        rootView = inflater.inflate(R.layout.listview, container,false);

        LinearLayout linlaHeaderProgress = (LinearLayout)rootView.findViewById(R.id.progress);


        listView = (ListView) rootView.findViewById(R.id.listView);


        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity ,feedItems);


        faActivity.setProgressBarIndeterminateVisibility(true);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);

        rootView.setVisibility(android.view.View.VISIBLE);

        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

        try {

            update();
        }catch (NullPointerException e){
            e.printStackTrace();
        }



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

                        Toast.makeText(getActivity(),"There are no routes locally stored", Toast.LENGTH_LONG).show();
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

            rootView.findViewById(R.id.pbHeaderProgress).setVisibility(View.GONE);

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

        searchView.setQueryHint("Enter Route#, Origin, Destination, Via or Type");
        searchView.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {
                listAdapter.getOriginalfeedItems();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {return true;}

            @Override
            public boolean onQueryTextChange(String newText) {

                if(listAdapter.getOriginalListCount()>0){
                    listAdapter.getFilter().filter(newText);
                }

               return true;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FeedItem f = (FeedItem)parent.getAdapter().getItem(position);

        Bundle bundle = new Bundle();
        bundle.putString("route", f.getRoute());

        timetable = TimeTable.newInstance(1,timetable.ARG_STRING);
        timetable.setArguments(bundle);
        FragmentManager fm4 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft4 = fm4.beginTransaction();

        if(timetable.isAdded()){
            ft4.show(timetable);
        }else{
            ft4.replace(R.id.container,timetable,timetable.ARG_STRING);

        }
        ft4.addToBackStack(null);
        ft4.commit();


    }

    public static Route newInstance(int someInt, String s){

        Route rFragment = new Route();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        rFragment.setArguments(args);
        return rFragment;
    }
}
