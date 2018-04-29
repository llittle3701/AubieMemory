package edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers.R;

public class ScoresFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scores, container, false);

        TextView beginnerScore = v.findViewById(R.id.beginner_score);
        TextView intermediateScore = v.findViewById(R.id.intermediate_score);
        TextView expertScore = v.findViewById(R.id.expert_score);

        Context context = getContext();
        HighScoreManager mHighScoreManager = new HighScoreManager(context);
        String beginnerHighScore = mHighScoreManager.getHighScore("BEGINNER").toString();
        String intermediateHighScore = mHighScoreManager.getHighScore("INTERMEDIATE").toString();
        String expertHighScore = mHighScoreManager.getHighScore("EXPERT").toString();

        beginnerScore.setText(beginnerHighScore);
        intermediateScore.setText(intermediateHighScore);
        expertScore.setText(expertHighScore);

        return v;
    }

}
