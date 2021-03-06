package edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers.R;

public class MemoryActivity extends AppCompatActivity implements MainFragment.MainMenuButtonListener {

    private Fragment fragment_main;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        mFragmentManager = getSupportFragmentManager();
        fragment_main = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment_main == null) {
            fragment_main = new MainFragment();
            mFragmentManager.beginTransaction().add(R.id.fragment_container, fragment_main).commit();
        }
    }

    @Override
    public void onBeginnerButtonPressed(Bundle bundle) {
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onIntermediateButtonPressed(Bundle bundle) {
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onExpertButtonPressed(Bundle bundle) {
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onScoreButtonPressed() {
        ScoresFragment scoresFragment = new ScoresFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, scoresFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStackImmediate();
        else super.onBackPressed();
    }
}
