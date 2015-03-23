package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import capstoneproject.jatransit.Adapter.ImageAdapter;
import capstoneproject.jatransit.MainActivity;
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
    private NFCBusTicket nfcBusTicket;
    private Route route;
    private AboutUs AboutUs;
    private TripPlanner tripPlanner;
    private Payment pay;
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


        ((GridView) rootView.findViewById(R.id.option))
                .setAdapter(new ImageAdapter(getActivity()));

        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText("HOME");
        gridViewListener();//handles the grid view listener

        return rootView;
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

                               text.setText( nearby.ARG_STRING);

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



                                text.setText(route.ARG_STRING);
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


                                text.setText(tripPlanner.ARG_STRING);
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



                                text.setText(map.ARG_STRING);
                                break;
                            case 4:
                                //Set up widget
                                Toast.makeText(getActivity(), "search",
                                        Toast.LENGTH_SHORT).show();
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




                                text.setText(faq.ARG_STRING);
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
                                text.setText(AboutUs.ARG_STRING);
                                break;

                            case 7:

                                pay =Payment.newInstance(7, pay.ARG_STRING);
                                FragmentManager fm7 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft7 = fm7.beginTransaction();


                                if (pay.isAdded()) {
                                    ft7.show(pay);
                                } else {
                                    ft7.replace(R.id.container, pay,pay.ARG_STRING);
                                }
                                ft7.addToBackStack(null);
                                ft7.commit();


                                text.setText(pay.ARG_STRING);
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


    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() == 0) {

        }
        else {

            text.setText("HOME");
        }
    }
}
