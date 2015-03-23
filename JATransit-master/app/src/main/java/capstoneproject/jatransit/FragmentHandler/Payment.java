package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/07/2015.
 */
public class Payment extends Fragment {

    public static final String ARG_STRING = "Payment";

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.payment,container,false);
        return rootView;
    }


    public static Payment newInstance(int someInt, String s) {
        Payment pfragment = new Payment();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        pfragment.setArguments(args);
        return pfragment;
    }
}
