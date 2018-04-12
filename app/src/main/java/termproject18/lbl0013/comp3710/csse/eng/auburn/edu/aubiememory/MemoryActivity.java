package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class MemoryActivity extends AppCompatActivity {

    private Fragment fragment_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        FragmentManager fm = getSupportFragmentManager();
        fragment_game = fm.findFragmentById(R.id.fragment_container);

        if (fragment_game == null) {
            fragment_game = new GameFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment_game).commit();
        }

    }

}
