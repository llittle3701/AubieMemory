package edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers.R;

public class MainFragment extends Fragment {

    private Button mBeginnerButton;
    private Button mIntermediateButton;
    private Button mExpertButton;
    private Button mScoreButton;

    MainMenuButtonListener mCallback;
    public interface MainMenuButtonListener {
        void onBeginnerButtonPressed(Bundle bundle);
        void onIntermediateButtonPressed(Bundle bundle);
        void onExpertButtonPressed(Bundle bundle);
        void onScoreButtonPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mBeginnerButton = v.findViewById(R.id.beginner_button);
        mBeginnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Difficulty", GameFragment.Difficulty.BEGINNER);
                mCallback.onBeginnerButtonPressed(bundle);
            }
        });

        mIntermediateButton = v.findViewById(R.id.intermediate_button);
        mIntermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Difficulty", GameFragment.Difficulty.INTERMEDIATE);
                mCallback.onIntermediateButtonPressed(bundle);
            }
        });

        mExpertButton = v.findViewById(R.id.expert_button);
        mExpertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Difficulty", GameFragment.Difficulty.EXPERT);
                mCallback.onExpertButtonPressed(bundle);
            }
        });

        mScoreButton = v.findViewById(R.id.scores_button);
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onScoreButtonPressed();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            if (context instanceof Activity) {
                mCallback = (MainMenuButtonListener) context;
            }
            else {
                throw new NullPointerException();
            }
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement MainMenuButtonListener");
        }
    }

}
