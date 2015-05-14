package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 05/14/2015.
 */
public class Help extends Fragment {
    public static final String ARG_STRING = "Help";

    private View rootView;
    private TextView nearby;
    private TextView routes;
    private TextView tripplanner;
    private TextView map;
    private TextView search;

    private TextView detail1;
    private TextView detail2;
    private TextView detail3;
    private TextView detail4;
    private TextView detail5;
    private TextView detail6;

    private TextView detail7;


    private TextView text;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.help,container,false);

        nearby = (TextView)rootView.findViewById(R.id.nearby);
        detail1 = (TextView)rootView.findViewById(R.id.detail1);

        routes = (TextView)rootView.findViewById(R.id.routes);
        detail2 = (TextView)rootView.findViewById(R.id.detail2);

        tripplanner = (TextView)rootView.findViewById(R.id.tripplanner);
        detail3 = (TextView)rootView.findViewById(R.id.detail3);

        map = (TextView)rootView.findViewById(R.id.map);
        detail4 = (TextView)rootView.findViewById(R.id.detail4);
        search = (TextView)rootView.findViewById(R.id.search);
        detail5 = (TextView)rootView.findViewById(R.id.detail5);


        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

        addContent();
        return rootView;
    }


    private void addContent() {
        String[] details = getActivity().getResources().getStringArray(R.array.details);
        String[] icon = getActivity().getResources().getStringArray(R.array.icon);

        nearby.setText(icon[0]);
        detail1.setText(details[0]);

        routes.setText(icon[1]);
        detail2.setText(details[1]);

        tripplanner.setText(icon[2]);
        detail3.setText(details[2]);

        map.setText(icon[3]);
        detail4.setText(details[3]);

        search.setText(icon[4]);
        detail5.setText(details[3]);
    }
}
