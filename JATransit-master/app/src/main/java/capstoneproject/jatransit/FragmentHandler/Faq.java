package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import capstoneproject.jatransit.R;

/**
 * Created by Caliph Cole on 03/05/2015.
 */
public class Faq extends Fragment {

    public static final String ARG_STRING = "FAQ";

    private View rootView;
    private TextView question1;
    private TextView answer1;
    private TextView question2;
    private TextView answer2;
    private TextView question3;
    private TextView answer3;
    private TextView text;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.faq,container,false);

        question1 = (TextView)rootView.findViewById(R.id.question1);
        answer1 = (TextView)rootView.findViewById(R.id.answer1);

        question2 = (TextView)rootView.findViewById(R.id.question2);
        answer2 = (TextView)rootView.findViewById(R.id.answer2);

        question3 = (TextView)rootView.findViewById(R.id.question3);
        answer3 = (TextView)rootView.findViewById(R.id.answer3);


        text = new TextView(getActivity());
        text = (TextView) getActivity().findViewById(R.id.title);
        text.setText(ARG_STRING);

        addContent();
        return rootView;
    }

    private void addContent() {
        String [] questions = getActivity().getResources().getStringArray(R.array.questions);
        String [] answers = getActivity().getResources().getStringArray(R.array.answers);


            question1.setText(questions[0]);
            answer1.setText(answers[0]);

        question2.setText(questions[1]);
        answer2.setText(answers[1]);

        question3.setText(questions[2]);
        answer3.setText(answers[2]);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                getActivity().finish();
                return true;


            case R.id.action_search:



                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static Faq newInstance(int someInt, String s) {
        Faq ffragment = new Faq();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        ffragment.setArguments(args);
        return ffragment;
    }

}
