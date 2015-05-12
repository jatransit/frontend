package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import capstoneproject.jatransit.Adapter.ImageAdapter;
import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/04/2015.
 */
public class HomeScreen extends Fragment {


    public static final String ARG_STRING = "HOME";
    private View rootView;

    /**
     * Initialization of all the fragments
     */
    private Nearby nearby;

    private MapsFragment map;
    private Faq faq;

    private Route route;
    private AboutUs AboutUs;
    private TripPlanner tripPlanner;
    private Search search;

    private TextView text;


    /**
     * SetUp GridView for home screen
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.homescreen, container,
                false);
        setHasOptionsMenu(true);


        ((GridView) rootView.findViewById(R.id.option))
                .setAdapter(new ImageAdapter(getActivity()));

        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText("HOME");
        gridViewListener();//handles the grid view listener



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
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

    /**
     * Handles the position selection on the homeScreen
     */
    public void gridViewListener(){
        ((GridView) rootView.findViewById(R.id.option))
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                        switch(position){

                            case 0:

                                nearby = Nearby.newInstance(0, nearby.ARG_STRING);
                                FragmentManager fm0 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft0 = fm0.beginTransaction();


                                if (nearby.isAdded()) {
                                    ft0.show(nearby);
                                } else {
                                    ft0.replace(R.id.container, nearby, nearby.ARG_STRING);
                                }
                                ft0.addToBackStack(null);
                                ft0.commit();

                                break;
                            case 1:



                                    route = Route.newInstance(1, route.ARG_STRING);
                                    FragmentManager fm1 = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft1 = fm1.beginTransaction();


                                    if (route.isAdded()) {
                                        ft1.show(route);
                                    } else {
                                        ft1.replace(R.id.container, route, route.ARG_STRING);
                                    }
                                    ft1.addToBackStack(null);
                                    ft1.commit();

                                break;
                            case 2:

                                tripPlanner = TripPlanner.newInstance(2, tripPlanner.ARG_STRING);
                                FragmentManager fm2 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft2 = fm2.beginTransaction();


                                if (tripPlanner.isAdded()) {
                                    ft2.show(tripPlanner);
                                } else {
                                    ft2.replace(R.id.container, tripPlanner, tripPlanner.ARG_STRING);
                                }
                                ft2.addToBackStack(null);
                                ft2.commit();

                                break;
                            case 3:
                                map = MapsFragment.newInstance(3, map.ARG_STRING);
                                FragmentManager fm3 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft3 = fm3.beginTransaction();


                                if (map.isAdded()) {
                                    ft3.show(map);
                                } else {
                                    ft3.replace(R.id.container, map, map.ARG_STRING);
                                }
                                ft3.addToBackStack(null);
                                ft3.commit();

                                break;
                            case 4:
                                search = Search.newInstance(4,search.ARG_STRING);
                                FragmentManager fm4 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft4 = fm4.beginTransaction();

                                if(search.isAdded()){
                                    ft4.show(search);
                                }else{
                                    ft4.replace(R.id.container,search,search.ARG_STRING);

                                }
                                ft4.addToBackStack(null);
                                ft4.commit();

                                break;
                            case 5:

                                faq = Faq.newInstance(5, faq.ARG_STRING);
                                FragmentManager fm5 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft5 = fm5.beginTransaction();


                                if (faq.isAdded()) {
                                    ft5.show(faq);
                                } else {
                                    ft5.replace(R.id.container, faq, faq.ARG_STRING);
                                }
                                ft5.addToBackStack(null);
                                ft5.commit();


                                break;
                            case 6:

                                AboutUs =AboutUs.newInstance(1, AboutUs.ARG_STRING);
                                FragmentManager fm6 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft6 = fm6.beginTransaction();


                                if (AboutUs.isAdded()) {
                                    ft6.show(AboutUs);
                                } else {
                                    ft6.replace(R.id.container, AboutUs,AboutUs.ARG_STRING);
                                }
                                ft6.addToBackStack(null);
                                ft6.commit();

                                break;



                        }
                    }
                });

    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public static HomeScreen newInstance(int someInt, String someTitle) {
        HomeScreen hfragment = new HomeScreen();
        Bundle args = new Bundle();
        args.putInt("homescreen", someInt);
        hfragment.setArguments(args);
        return hfragment;
    }



}
