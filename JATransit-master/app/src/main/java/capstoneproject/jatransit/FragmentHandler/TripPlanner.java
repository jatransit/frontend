package capstoneproject.jatransit.FragmentHandler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import capstoneproject.jatransit.R;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class TripPlanner extends Fragment implements View.OnClickListener {

    public static final String ARG_STRING = "TripPlanner";
    private static final String TAG = HomeScreen.class.getSimpleName();

    private String URL_FEED ="http://jatransit.appspot.com/routes";

    private View rootView;

    private MaterialEditText textOrigin;
    private MaterialEditText textDestination;
    private Button search;
    private List<FeedItem> feedItems;

    private String[] values = new String[] { "24ex:Hellshire:Marcus Garvey Drive:City", "900:Hellshire:Hagley Pk Road:Half Way Tree", "75:Greater Portmore:Gregory Park:Spanish Town",
            "78:Christian Gardens:Gregory Pk.Hagley Pk Road:Half Way Tree", "68:Greater Portmore :Gregory Park:Spanish Town ", "50ex:Christian Gardens : Gregory Pk.Hagley Pk Road : Half Way Tree", "75ex:Christian Gardens: Gregory Pk.Hagley Pk Road : Half Way Tree", "500:Greater Portmore:Gregory Park:Spanish Town",
            "31:Hellshire :Hagley Pk Road: Half Way Tree", "32B:Greater Portmore:Gregory Park:Spanish Town" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tripplanner,container,false);
        textOrigin = (MaterialEditText)rootView.findViewById(R.id.origin);
        textDestination = (MaterialEditText)rootView.findViewById(R.id.destination);
        search = (Button)rootView.findViewById(R.id.tripsearch);
        retrieveAllRoutes();

        search.setOnClickListener(this);


        return rootView;
    }

    private void retrieveAllRoutes() {
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
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("routes");


            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();

                item.setOrigin(feedObj.getString("origin"));
                item.setDestination(feedObj.getString("destination"));
                item.setRoute(feedObj.getString("route"));
                item.setVia(feedObj.getString("via"));

                item.setRouteType(feedObj.getString("route_type"));

                feedItems.add(0, item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static TripPlanner newInstance(int someInt, String s) {

        TripPlanner tfragment = new TripPlanner();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        tfragment.setArguments(args);
        return tfragment;
    }

    @Override
    public void onClick(View v) {

        // retrieve the text from the textfield
        String origin = textOrigin.getText().toString();
        String destine = textDestination.getText().toString();

        // error handling
        if(origin.isEmpty() || destine.isEmpty()){

            Toast.makeText(getActivity(),"Ïnvalid Data",Toast.LENGTH_SHORT).show();
            Log.d("myTag", feedItems.get(0).toString());
        }else{

            // Query database for valid routes

           for (int i = 0 ; i< feedItems.size();i++){


               if(origin.substring(0,3).equals(feedItems.get(i).toString().substring(0, 3))){

                   //if(destine.equals((values[i].split(":"))[3]))
                   Toast.makeText(getActivity(),values[i],Toast.LENGTH_SHORT).show();
               }

           }
        }

    }
}
