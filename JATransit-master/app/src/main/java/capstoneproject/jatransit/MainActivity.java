package capstoneproject.jatransit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import capstoneproject.jatransit.FragmentHandler.HomeScreen;
import capstoneproject.jatransit.FragmentHandler.MapsFragment;
import capstoneproject.jatransit.FragmentHandler.Settings;
import capstoneproject.jatransit.data.DBHelper;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by CaliphCole on 02/17/2015.
 */
public class MainActivity extends ActionBarActivity{

    private static final String TAG = "MainActivity";



    private MapsFragment maps;

    private Settings settings;
    private HomeScreen home;
    public TextView text;
    public DBHelper routedb;
    public   List<FeedItem> res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);//main settings



        /**
         * Center title of actionbar
         */

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D4AF37")));


        /**
         * Setting up the home screen fragment
         */
        home = HomeScreen.newInstance(0, "hello world");
        FragmentManager fragmentManager0 = getSupportFragmentManager();
        fragmentManager0.beginTransaction().add(R.id.container, home,"hello world").addToBackStack(null).commit();


    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                //finish();
                return true;

            case R.id.settings:

               /* settings = Settings.newInstance(1, settings.ARG_STRING);
                FragmentManager fm9 = getSupportFragmentManager();
                FragmentTransaction ft9 = fm9.beginTransaction();


                if (settings.isAdded()) {
                    ft9.show(settings);
                } else {
                    ft9.replace(R.id.container, settings, settings.ARG_STRING);
                }

                ft9.commit();*/
                return true;

            case R.id.action_search:



                return true;

        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    public void onBackPressed() {

        if(home.isVisible()){

            finish();
        }else

            super.onBackPressed();

    }
}
