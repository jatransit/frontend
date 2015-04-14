package capstoneproject.jatransit.FragmentHandler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

import capstoneproject.jatransit.R;

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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tripplanner,container,false);
        textOrigin = (MaterialEditText)rootView.findViewById(R.id.origin);
        textDestination = (MaterialEditText)rootView.findViewById(R.id.destination);
        search = (Button)rootView.findViewById(R.id.tripsearch);


        search.setOnClickListener(this);


        return rootView;
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
        if (!origin.isEmpty() && !destine.isEmpty()) {

            Bundle bundle = new Bundle();
            bundle.putString("origin", origin);
            bundle.putString("destination", destine);

            Searchfragment searchfragment =Searchfragment.newInstance(1,"");

            searchfragment.setArguments(bundle);

            FragmentManager fm5 = getActivity().getSupportFragmentManager();
            FragmentTransaction ft5 = fm5.beginTransaction();


            if (searchfragment.isAdded()) {
                ft5.show(searchfragment);
            } else {
                ft5.replace(R.id.container, searchfragment, "");
            }
            ft5.addToBackStack(null);
            ft5.commit();

        }
    }
}
