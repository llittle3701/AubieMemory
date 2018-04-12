package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

public class GameFragment extends Fragment {

    private final String ERROR_TAG = "error";
    private final int TONE_LENGTH = 1000; //tone length in milliseconds
    private final int TONE_INTERVAL = 2000; //interval between each tone
    private static final Random RANDOM = new Random();

    private enum Color {BLUE, RED, YELLOW, GREEN}
    private Color[] colors = Color.values();
    private enum GamePhase {PLAYER, CPU};

    private ArrayList<Color> mToneSequence;
    private int mCurrentToneIndex = 0;

    private int mScore;

    private Button mBlueButton;
    private Button mRedButton;
    private Button mYellowButton;
    private Button mGreenButton;
    private Button mStartGameButton;

    private MediaPlayer mBlueNote;
    private MediaPlayer mRedNote;
    private MediaPlayer mYellowNote;
    private MediaPlayer mGreenNote;
    private MediaPlayer mVictorySound;
    private MediaPlayer mLoseSound;

    private TextView mScoreView;
    private TextView mMessageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_game, container, false);

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mToneSequence = new ArrayList<>();
        mScoreView = v.findViewById(R.id.score);
        mMessageView = v.findViewById(R.id.message);

        mBlueButton = v.findViewById(R.id.blue_button);
        mRedButton =  v.findViewById(R.id.red_button);
        mYellowButton = v.findViewById(R.id.yellow_button);
        mGreenButton = v.findViewById(R.id.green_button);
        mStartGameButton = v.findViewById(R.id.start_button);

        setGamePhase(GamePhase.CPU);

        mBlueNote = MediaPlayer.create(getActivity(), R.raw.blue_long);
        mBlueNote.setVolume(0.05f, 0.05f);
        mRedNote = MediaPlayer.create(getActivity(), R.raw.red_long);
        mRedNote.setVolume(0.05f, 0.05f);
        mYellowNote = MediaPlayer.create(getActivity(), R.raw.yellow_long);
        mYellowNote.setVolume(0.05f, 0.05f);
        mGreenNote = MediaPlayer.create(getActivity(), R.raw.green_long);
        mGreenNote.setVolume(0.05f, 0.05f);
        mVictorySound = MediaPlayer.create(getActivity(), R.raw.victory);
        mVictorySound.setVolume(0.05f, 0.05f);
        mLoseSound = MediaPlayer.create(getActivity(), R.raw.lose);
        mLoseSound.setVolume(0.05f, 0.05f);

        mVictorySound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                try {
                    mp.stop();
                    mp.prepare();
                }
                catch(IOException e){
                    Log.e(ERROR_TAG, "IOException for victory or lose sound.");
                }
            }
        });

        mLoseSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                try {
                    mp.stop();
                    mp.prepare();
                }
                catch(IOException e){
                    Log.e(ERROR_TAG, "IOException for victory or lose sound.");
                }
            }
        });


        mBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mBlueButton, mBlueNote, "blue", GamePhase.PLAYER);
                checkCorrectInput(Color.BLUE);
            }
        });

        mRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mRedButton, mRedNote, "red", GamePhase.PLAYER);
                checkCorrectInput(Color.RED);
            }
        });

        mYellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mYellowButton, mYellowNote, "yellow", GamePhase.PLAYER);
                checkCorrectInput(Color.YELLOW);
            }
        });

        mGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mGreenButton, mGreenNote, "green", GamePhase.PLAYER);
                checkCorrectInput(Color.GREEN);
            }
        });

        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Wait some time, then play tone sequence for player
                setGamePhase(GamePhase.CPU);

                mMessageView.setText("");
                setScore(0);
                mCurrentToneIndex = 0;
                mToneSequence.clear();

                new Handler().postDelayed(new Runnable(){public void run() { computerPlayToneSequence();}}, TONE_INTERVAL);
            }
        });

        return v;
    }

    private void playTone(final Button button, final MediaPlayer note,
                          final String text, final GamePhase phase) {

        final Boolean isLastInSequence = (mCurrentToneIndex == (mToneSequence.size() - 1));
        button.setText("Beep");
        setGamePhase(GamePhase.CPU);
        //play for tone for its duration
        note.start();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    note.stop();
                    note.prepare();
                    button.setText(text);
                    if (phase == GamePhase.PLAYER) {
                        if (isLastInSequence)
                            setGamePhase(GamePhase.CPU);
                        else
                            setGamePhase(GamePhase.PLAYER);
                    }
                }
                catch(IOException e){
                    Log.e(ERROR_TAG, "IOException for button.");
                }
            }
        }, TONE_LENGTH);
    }


    private void computerPlayTone(Color color) {
        switch (color) {
            case BLUE:
                playTone(mBlueButton, mBlueNote, color.toString(), GamePhase.CPU);
                break;
            case RED:
                playTone(mRedButton, mRedNote, color.toString(), GamePhase.CPU);
                break;
            case YELLOW:
                playTone(mYellowButton, mYellowNote, color.toString(), GamePhase.CPU);
                break;
            case GREEN:
                playTone(mGreenButton, mGreenNote, color.toString(), GamePhase.CPU);
                break;
            default:
        }
    }

    // Adds a new tone to the sequence, plays each tone, then allows the
    // player to input the tones.
    private void computerPlayToneSequence() {
        //add a new tone to sequence
        Color next = colors[RANDOM.nextInt(colors.length)];
        mToneSequence.add(next);

        new CountDownTimer(30000, TONE_LENGTH + TONE_INTERVAL) {
            private int index = 0;
            public void onTick(long millisUntilFinished) {
                if (index < mToneSequence.size()) {
                    computerPlayTone(mToneSequence.get(index));
                    index++;
                }
                else {
                    this.cancel();
                    this.onFinish();
                }
            }

            public void onFinish() {
                if (index < (mToneSequence.size())) {
                    this.start();
                }
                else {
                    setGamePhase(GamePhase.PLAYER);
                    //something
                }
            }
        }.start();
    }

    // Sets the phase of the game. There are two phases:
    // 1. The computer (CPU) plays tones for the player to memorize. Player input disabled.
    // 2. The player inputs the tones
    private void setGamePhase(GamePhase phase) {
        switch (phase) {
            case CPU:
                mBlueButton.setEnabled(false);
                mRedButton.setEnabled(false);
                mYellowButton.setEnabled(false);
                mGreenButton.setEnabled(false);
                break;
            case PLAYER:
                mBlueButton.setEnabled(true);
                mRedButton.setEnabled(true);
                mYellowButton.setEnabled(true);
                mGreenButton.setEnabled(true);
                break;
            default:
        }
    }

    private void checkCorrectInput(Color color) {
        //is the inputted tone correct?
        //mInputSequence.add(color);
        if (color == mToneSequence.get(mCurrentToneIndex)) {
            mCurrentToneIndex++;
        }
        else {
            //player chose wrong. Wait for selected tone to finish, then play lose sound.
            new Handler().postDelayed(new Runnable() { public void run() {mLoseSound.start();}}, TONE_LENGTH);
            mToneSequence.clear();
            mCurrentToneIndex = 0;
            mMessageView.setText("Too bad. Better luck next time.");
            return;
        }
        //has the player won the round?
        if (mCurrentToneIndex == mToneSequence.size()) {
            //player inputted sequence correctly. Wait for selected tone to finish, then play victory sound.
            new Handler().postDelayed(new Runnable() { public void run() {mVictorySound.start();}}, TONE_LENGTH);
            setGamePhase(GamePhase.CPU);
            mCurrentToneIndex = 0;
            //play victory sound
            setScore(mToneSequence.size());
            //Wait some time, then play new tone sequence for player
            new Handler().postDelayed(new Runnable() { public void run() { computerPlayToneSequence();}}, TONE_INTERVAL + TONE_LENGTH);
        }
    }

    private void setScore(Integer score) {
        mScore = score;
        mScoreView.setText(score.toString());
    }
}
