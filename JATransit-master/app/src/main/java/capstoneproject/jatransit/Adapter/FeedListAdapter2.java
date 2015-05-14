package capstoneproject.jatransit.Adapter;

/**
 * Created by Caliph Cole on 03/22/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import capstoneproject.jatransit.FragmentHandler.Calculations;
import capstoneproject.jatransit.FragmentHandler.Route;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.data.FeedItem;

public class FeedListAdapter2 extends BaseAdapter implements Filterable{

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private Route routeFragment;
    private RouteFilter filter;
    private List<FeedItem> originalfeedItems;
    private Calculations cal;

    public FeedListAdapter2(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.originalfeedItems = feedItems;

        getFilter();
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.nearby, null);

        TextView route = (TextView) convertView.findViewById(R.id.route);
        TextView origin = (TextView) convertView.findViewById(R.id.org);

        TextView des = (TextView) convertView.findViewById(R.id.des);

        TextView currentLocation = (TextView)convertView.findViewById(R.id.currentlocation);
        TextView distance = (TextView)convertView.findViewById(R.id.distance);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

        FeedItem item = feedItems.get(position);

        route.setText(item.getRoute());
        origin.setText(item.getOrigin());

           des.setText(item.getDestination());

            String s = item.getDistance();
            Log.d("Distance",s);
            distance.setText(s);
            timestamp.setText(item.getTimeStamp());
            currentLocation.setText(item.getCurrentlocation());







        /*// Converting timestamp into x ago format
        try {
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(item.getTimeStamp()) * 1000L,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);


            timestamp.setText(timeAgo);
        }catch (Exception e){
            e.printStackTrace();
        }*/

        return convertView;
    }

    @Override
    public Filter getFilter() {

        if(filter == null){

            filter = new RouteFilter();
        }

        return filter;
    }

    //Getter method that handles the
    public boolean getOriginalfeedItems(){

        feedItems = originalfeedItems;
        notifyDataSetChanged();
        return true;
    }

    public int getOriginalListCount() {
        return originalfeedItems.size();
    }

    private class RouteFilter extends Filter{

        /*
        Inner class that handles the filtering of the listview
         */

        @Override
        protected FilterResults performFiltering (CharSequence constraint){
            constraint = constraint. toString().toLowerCase();
            FilterResults results = new FilterResults();
            List<FeedItem> filteredItems = new ArrayList<FeedItem>();
            if(constraint != null && constraint.toString().length() > 0) {

                if (originalfeedItems != null && originalfeedItems.size() > 0) {

                    for (FeedItem a : originalfeedItems) {
                        if (a.getRoute().toLowerCase().contains(constraint.toString())){
                            Log.d("Tag1", constraint.toString());
                            filteredItems.add(a);
                        }else if(a.getOrigin().toLowerCase().contains(constraint.toString())){
                            Log.d("Tag2", a.getOrigin());
                            filteredItems.add(a);
                        }else if( a.getDestination().toLowerCase().contains(constraint.toString())){
                            Log.d("Tag3", constraint.toString());
                            filteredItems.add(a);
                        }else if(a.getVia().toLowerCase().contains(constraint.toString())) {
                            Log.d("Tag4", constraint.toString());
                            filteredItems.add(a);
                        }else if(a.getRoutetype().toLowerCase().contains(constraint.toString())){
                            filteredItems.add(a);
                        }


                    }
                    results.values = filteredItems;
                }

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            try {
                feedItems = (List<FeedItem>) results.values;
                notifyDataSetChanged();
            }catch (Exception e){
                feedItems = originalfeedItems;
                notifyDataSetChanged();
            }
        }
    }

}