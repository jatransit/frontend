package capstoneproject.jatransit.FragmentHandler;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
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
    private TextView text;

    private MaterialEditText textOrigin;
    private MaterialEditText textDestination;
    private ButtonRectangle search;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.tripplanner,container,false);
        textOrigin = (MaterialEditText)rootView.findViewById(R.id.origin);
        textDestination = (MaterialEditText)rootView.findViewById(R.id.destination);
        search = (ButtonRectangle)rootView.findViewById(R.id.tripsearch);


        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

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

            TripPlannerQueryFragment tripPlannerQueryFragment = TripPlannerQueryFragment.newInstance(1, "");

            tripPlannerQueryFragment.setArguments(bundle);

            FragmentManager fm5 = getActivity().getSupportFragmentManager();
            FragmentTransaction ft5 = fm5.beginTransaction();


            if (tripPlannerQueryFragment.isAdded()) {
                ft5.show(tripPlannerQueryFragment);
            } else {
                ft5.replace(R.id.container, tripPlannerQueryFragment, "");
            }
            ft5.addToBackStack(null);
            ft5.commit();

            text.setText(ARG_STRING);
        }else {

            textOrigin.setText("");
            textDestination.setText("");
            textOrigin.setHint("Please enter an origin");
            textDestination.setHint("Please enter a Destination");
            textDestination.setHintTextColor(getResources().getColor(R.color.hint));
            textOrigin.setHintTextColor(getResources().getColor(R.color.hint));

        }
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
}
