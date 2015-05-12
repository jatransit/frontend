package capstoneproject.jatransit.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import capstoneproject.jatransit.FragmentHandler.HomeScreen;
import capstoneproject.jatransit.MainActivity;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.app.AppController;
import capstoneproject.jatransit.data.DBHelper;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 6000;

    private String URL_FEED ="http://jatransit.appspot.com/routes";

    private static final String TAG = HomeScreen.class.getSimpleName();


    public DBHelper routedb;

    private Timer myTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);


        routedb = new DBHelper(this);







       // new Handler().postDelayed(new Runnable() {
        myTimer = new Timer(); //Set up a timer, to execute TimerMethod repeatedly
        myTimer.schedule(new TimerTask() {
            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                update();
                // close this activity
                finish();


            }
        },SPLASH_TIME_OUT);
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

            routedb = new DBHelper(this);

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);


                if (feedArray.length() > routedb.numberOfRows()) {

                    routedb.insertRoute(feedObj.getString("route"), feedObj.getString("origin"), feedObj.getString("destination"), feedObj.getString("via"), feedObj.getString("route_type"));

                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
