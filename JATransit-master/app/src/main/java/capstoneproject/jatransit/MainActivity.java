package capstoneproject.jatransit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import capstoneproject.jatransit.FragmentHandler.Help;
import capstoneproject.jatransit.FragmentHandler.HomeScreen;
import capstoneproject.jatransit.FragmentHandler.MapsFragment;
import capstoneproject.jatransit.data.DBHelper;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by CaliphCole on 02/17/2015.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";



    private MapsFragment maps;


    private HomeScreen home;
    public TextView text;
    public DBHelper routedb;
    public   List<FeedItem> res;
    int count = 1;

    public FloatingActionButton button;


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

            button = (FloatingActionButton) findViewById(R.id.help);

        button.setOnClickListener(this);



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
                finish();
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

    @Override
    public void onClick(View v) {

        if(count ==1) {
            Help help = Help.newInstance(1, "");


            FragmentManager fm5 = getSupportFragmentManager();
            FragmentTransaction ft5 = fm5.beginTransaction();


            if (help.isAdded()) {
                ft5.show(help);
            } else {
                ft5.replace(R.id.container, help, "");
            }
            ft5.addToBackStack(null);
            ft5.commit();
            count++;
        }else{
            count =1;
            super.onBackPressed();
        }
    }
}
