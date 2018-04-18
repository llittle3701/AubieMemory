package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

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
    public void onScoreButtonPressed(Bundle bundle) {

    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStackImmediate();
        else super.onBackPressed();
    }
}
