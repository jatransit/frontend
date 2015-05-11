package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 05/10/2015.
 */
public class Settings extends Fragment {

    public static final String ARG_STRING = "Setting";
    View rootView;
    TextView text;
    MaterialEditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.settings, container, false);

        text = (TextView)rootView.findViewById(R.id.crashreport);
        editText = (MaterialEditText)rootView.findViewById(R.id.reporttextbox);


        return rootView;
    }

    public static Settings newInstance(int someInt, String s) {
        Settings sfragment = new Settings();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        sfragment.setArguments(args);
        return sfragment;
    }
}
