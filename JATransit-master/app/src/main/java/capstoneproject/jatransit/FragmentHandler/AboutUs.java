package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.about, container,false);

        body = (TextView)rootView.findViewById(R.id.body);

        addContent();
        return rootView;
    }

    private void addContent() {

        String bodyText = getString(R.string.body);
        body.setText(bodyText);
    }

    public static AboutUs newInstance(int someInt, String s) {
        AboutUs afragment = new AboutUs();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        afragment.setArguments(args);
        return afragment;
    }
}
