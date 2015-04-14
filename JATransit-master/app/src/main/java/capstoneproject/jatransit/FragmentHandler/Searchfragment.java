package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import capstoneproject.jatransit.Adapter.FeedListAdapter;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.data.DBHelper;
import capstoneproject.jatransit.data.FeedItem;

/**
 * Created by Caliph Cole on 04/13/2015.
 */
public class Searchfragment extends Fragment {

    public View rootView;
    private ListView listView;
    private FeedListAdapter listAdapter;

    private List<FeedItem> feedItems;
    private FragmentActivity faActivity;
    private FeedItem item;


    public DBHelper db;

    String s;
    public Searchfragment(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new DBHelper(getActivity());
        faActivity  = (FragmentActivity)    super.getActivity();
        rootView = inflater.inflate(R.layout.listview, container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        Bundle bundle = getArguments();
        String s = bundle.getString("query");
        String origin = bundle.getString("origin");
        String destine = bundle.getString("destination");


        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(faActivity ,feedItems);

        listView.setAdapter(listAdapter);


        rootView.setVisibility(android.view.View.VISIBLE);


        try{
            if (s!= null){
                query(s);
            }else{
                String temp = origin + " to " + destine;
                query(temp);
            }

        }catch (Exception e){

        }



        return rootView;

    }

    public void query(String s ) {

        db = new DBHelper(getActivity());

        List<FeedItem> res = db.getAllRoutesByQuery(s);
            for (int i = 0; i < res.size(); i++) {
                feedItems.add(0, res.get(i));
            }

            Log.d("Tag", "" + feedItems.size());


            listAdapter.notifyDataSetChanged();


    }

    public static Searchfragment newInstance(int someInt, String s){


        Searchfragment sFragment = new Searchfragment();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        sFragment.setArguments(args);
        return sFragment;
    }
}
