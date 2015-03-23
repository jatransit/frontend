package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class TripPlanner extends Fragment {

    public static final String ARG_STRING = "TripPlanner";

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tripplanner,container,false);


        return rootView;
    }

    public static TripPlanner newInstance(int someInt, String s) {

        TripPlanner tfragment = new TripPlanner();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        tfragment.setArguments(args);
        return tfragment;
    }
}
