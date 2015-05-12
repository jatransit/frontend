package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class AboutUs extends Fragment {

    public static final String ARG_STRING = "About Us";
    private View rootView;
    private TextView body;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.about, container,false);

        body = (TextView)rootView.findViewById(R.id.body);

        addContent();
        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);
        return rootView;
    }

    private void addContent() {

        String bodyText = getString(R.string.body);
        body.setText(bodyText);
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
    public static AboutUs newInstance(int someInt, String s) {
        AboutUs afragment = new AboutUs();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        afragment.setArguments(args);
        return afragment;
    }
}
