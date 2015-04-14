package capstoneproject.jatransit;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import capstoneproject.jatransit.FragmentHandler.HomeScreen;
import capstoneproject.jatransit.FragmentHandler.MapsFragment;
import capstoneproject.jatransit.FragmentHandler.Searchfragment;
import capstoneproject.jatransit.data.DBHelper;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by CaliphCole on 02/17/2015.
 */
public class MainActivity extends ActionBarActivity{

    private static final String TAG = "DemoActivity";



    private MapsFragment maps;

    private HomeScreen home;
    public TextView text;
    public DBHelper routedb;
    public   List<FeedItem> res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);//main layout



        /**
         * Center title of actionbar
         */

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff005fbf));


        /**
         * Setting up the home screen fragment
         */
        home = HomeScreen.newInstance(0, "hello world");
        FragmentManager fragmentManager0 = getSupportFragmentManager();
        fragmentManager0.beginTransaction().add(R.id.container, home,"hello world").addToBackStack(null).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {


                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                DBHelper db = new DBHelper(getApplication());

                Bundle bundle = new Bundle();
                bundle.putString("query", s);


                Searchfragment searchfragment = Searchfragment.newInstance(1,"search");
                searchfragment.setArguments(bundle);

                FragmentManager fm5 = getSupportFragmentManager();
                FragmentTransaction ft5 = fm5.beginTransaction();


                if (searchfragment.isAdded()) {
                    ft5.show(searchfragment);
                } else {
                    ft5.replace(R.id.container, searchfragment, "");
                }
                ft5.addToBackStack(null);
                ft5.commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:

                return true;

            case R.id.settings:

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
            home.onBackPressed();
            super.onBackPressed();

    }
}
