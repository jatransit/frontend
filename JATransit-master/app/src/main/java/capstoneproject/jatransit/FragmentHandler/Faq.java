package capstoneproject.jatransit.FragmentHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    private TextView question;
    private TextView answer;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.faq,container,false);

        question = (TextView)rootView.findViewById(R.id.question);
        answer = (TextView)rootView.findViewById(R.id.answer);

        addContent();
        return rootView;
    }

    private void addContent() {
        String [] questions = getActivity().getResources().getStringArray(R.array.questions);
        String [] answers = getActivity().getResources().getStringArray(R.array.answers);

        for (int i = 0 ; i<questions.length;i++){
            question.setText(questions[i]);
            answer.setText(answers[i]);
        }

    }

    public static Faq newInstance(int someInt, String s) {
        Faq ffragment = new Faq();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        ffragment.setArguments(args);
        return ffragment;
    }
}
