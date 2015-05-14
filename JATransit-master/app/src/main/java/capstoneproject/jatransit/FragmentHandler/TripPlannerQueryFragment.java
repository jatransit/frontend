package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import capstoneproject.jatransit.Adapter.FeedListAdapter;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.data.DBHelper;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by Caliph Cole on 04/13/2015.
 */
public class TripPlannerQueryFragment extends Fragment {

    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;
    private FeedItem item;
    private TextView notfound;


    public DBHelper db;

    String s;
    public TripPlannerQueryFragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new DBHelper(getActivity());
        faActivity  = (FragmentActivity)    super.getActivity();
        rootView = inflater.inflate(R.layout.listview, container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        notfound = (TextView) rootView.findViewById(R.id.message);
        Bundle bundle = getArguments();

        String origin = bundle.getString("origin");
        String destine = bundle.getString("destination");


        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity ,feedItems);

        listView.setAdapter(listAdapter);


        rootView.setVisibility(android.view.View.VISIBLE);




        query(origin, destine);

        rootView.findViewById(R.id.pbHeaderProgress).setVisibility(View.GONE);


        return rootView;

    }

    public void query(String o ,String d) {

        db = new DBHelper(getActivity());

        List<FeedItem> res = db.tripPlanner(o,d);
            for (int i = 0; i < res.size(); i++) {
                feedItems.add(0, res.get(i));
            }

            Log.d("Tag", "" + feedItems.size());

        if(feedItems.size()>0){
            listAdapter.notifyDataSetChanged();
        }else{

            notfound.setText("No Results found :-( ");
        }



    }

    public static TripPlannerQueryFragment newInstance(int someInt, String s){


        TripPlannerQueryFragment sFragment = new TripPlannerQueryFragment();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        sFragment.setArguments(args);
        return sFragment;
    }
}
