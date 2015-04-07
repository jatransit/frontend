package capstoneproject.jatransit.Adapter;

/**
 * Created by Caliph Cole on 03/22/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import capstoneproject.jatransit.FragmentHandler.Route;
import capstoneproject.jatransit.R;
import capstoneproject.jatransit.data.FeedItem;

public class FeedListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private Route routeFragment;

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
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
            convertView = inflater.inflate(R.layout.route, null);

        TextView route = (TextView) convertView.findViewById(R.id.route);
        TextView origin = (TextView) convertView.findViewById(R.id.org);
        TextView via = (TextView) convertView.findViewById(R.id.via);
        TextView des = (TextView) convertView.findViewById(R.id.des);
        TextView routetype = (TextView)convertView.findViewById(R.id.routetype);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

        FeedItem item = feedItems.get(position);

        route.setText(item.getRoute());
        origin.setText(item.getOrigin());
        via.setText(item.getVia());
        des.setText(item.getDestination());
        routetype.setText(item.getRoutetype());

        // Converting timestamp into x ago format
        try {
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(item.getTimeStamp()) * 1000L,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);


            timestamp.setText(timeAgo);
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

}