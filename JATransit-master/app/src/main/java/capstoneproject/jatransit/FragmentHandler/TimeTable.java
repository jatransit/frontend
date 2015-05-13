package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 05/12/2015.
 */
public class TimeTable extends Fragment {

    public static final String ARG_STRING = "TimeTable";
    private View rootView;
    private ImageView hellshire;
    private TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.timetable, container, false);


        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);
         Bundle bundle = getArguments();

        String s = bundle.getString("route");
        initialiseImage(s);


        return rootView;
    }


    private void initialiseImage(String s) {

        String imgname = "route"+ s.split(": ")[1].toLowerCase() + ".png";

        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)rootView.findViewById(R.id.imageView);
        imageView.setImage(ImageSource.asset(imgname));
    }

    public static TimeTable newInstance(int someInt, String s) {

        TimeTable tfragment = new TimeTable();
        Bundle args = new Bundle();
        args.putInt("Nearby", someInt);
        tfragment.setArguments(args);
        return tfragment;
    }
}
