package capstoneproject.jatransit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/04/2015.
 */


public class ImageAdapter extends BaseAdapter {

    private Context mContext;



    // references to our images
    private Integer[] mThumbIds1 = {R.drawable.nearby,R.drawable.route,R.drawable.trip,R.drawable.map,R.drawable.search,R.drawable.faq,R.drawable.terms} ;//resource link
    private String[] mThumbtext = {"Nearby","Routes","Trip","Map","Search","FAQ","About"};


    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds1.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {

       LayoutInflater inflater = ( LayoutInflater ) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder = new Holder();
        View rowView;

                rowView = inflater.inflate(R.layout.customgridview, null);
                rowView.setPadding(40, 40, 40, 40);
                holder.tv = (TextView)rowView.findViewById(R.id.label);
                holder.img = (ImageView)rowView.findViewById(R.id.homeimage);

            holder.tv.setText(mThumbtext[position]);
            holder.img.setImageResource(mThumbIds1[position]);


        return rowView;

   }
}
