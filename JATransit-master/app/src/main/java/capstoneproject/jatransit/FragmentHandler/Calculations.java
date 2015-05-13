package capstoneproject.jatransit.FragmentHandler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 05/12/2015.
 */
public class Calculations extends Fragment implements LocationListener{


    private View rootView;
    LocationListener locationListener;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_maps,container,false);


        return rootView;
    }

    public double calculation(String lat, String longi){

        Location buslocation = new Location("");

        buslocation.setLatitude(Double.parseDouble(lat));
        buslocation.setLongitude(Double.parseDouble(longi));

        locationListener.onLocationChanged(buslocation);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double  c = location.distanceTo(buslocation);

        return c;
    }

    public static Calculations newInstance(int someInt, String s) {
       Calculations cfragment = new Calculations();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        cfragment.setArguments(args);
        return cfragment;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
