package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;

public class GameFragment extends Fragment {

    private final String ERROR_TAG = "error";
    private final String DEBUG_TAG = "debug";

    public enum Difficulty {BEGINNER, INTERMEDIATE, EXPERT}
    private String mCurrentDifficulty = "";

    private int TONE_LENGTH = 1000; //tone length in milliseconds
    private int TONE_INTERVAL = 2000; //interval between each tone

    //Helper classes
    ToneSequence mToneSequence;
    HighScoreManager mHighScoreManager;
    CountDownTimer mCountDownTimer;

    private int mScore;
    private String scoreFile = "high_score.xml";

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
    private TextView mHighScoreView;
    private TextView mMessageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_game, container, false);

        mToneSequence = new ToneSequence();
        Context context = getContext();
        if (context != null)
            mHighScoreManager = new HighScoreManager(context);

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mScoreView = v.findViewById(R.id.score);
        mHighScoreView = v.findViewById(R.id.highscore);
        mMessageView = v.findViewById(R.id.message);

        mBlueButton = v.findViewById(R.id.blue_button);
        mRedButton =  v.findViewById(R.id.red_button);
        mYellowButton = v.findViewById(R.id.yellow_button);
        mGreenButton = v.findViewById(R.id.green_button);
        mStartGameButton = v.findViewById(R.id.start_button);

        enableButtons(false);
        setDifficulty(getArguments());

        String highScore = mHighScoreManager.getHighScore(context, mCurrentDifficulty).toString();
        mHighScoreView.setText(highScore);

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
                playTone(mBlueButton, mBlueNote, "blue", true);
                checkCorrectInput(Color.BLUE);
            }
        });

        mRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mRedButton, mRedNote, "red", true);
                checkCorrectInput(Color.RED);
            }
        });

        mYellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mYellowButton, mYellowNote, "yellow", true);
                checkCorrectInput(Color.YELLOW);
            }
        });

        mGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mGreenButton, mGreenNote, "green", true);
                checkCorrectInput(Color.GREEN);
            }
        });

        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartGameButton.setVisibility(View.INVISIBLE);
                getFragmentManager().beginTransaction().detach(GameFragment.this).attach(GameFragment.this).commit();
            }
        });

        //Wait some time, then play tone sequence for player
        enableButtons(false);

        mMessageView.setText("");
        setScore(0);
        mToneSequence.resetToneIndex();
        mToneSequence.clear();

        new Handler().postDelayed(new Runnable(){public void run() { computerPlayToneSequence();}}, 2 * TONE_LENGTH);

        return v;
    }

    private void setDifficulty(Bundle bundle) {
        Difficulty difficulty = (Difficulty) bundle.get("Difficulty");
        switch (difficulty) {
            case BEGINNER:
                TONE_INTERVAL = 750;
                mCurrentDifficulty = Difficulty.BEGINNER.name();
                break;
            case INTERMEDIATE:
                TONE_INTERVAL = 500;
                mCurrentDifficulty = Difficulty.INTERMEDIATE.name();
                break;
            case EXPERT:
                TONE_INTERVAL = 50;
                mCurrentDifficulty = Difficulty.EXPERT.name();
                break;
            default:
        }
    }

    private void playTone(final Button button, final MediaPlayer note,
                          final String text, final boolean isPlayerTurn) {

        final Boolean isLastInSequence = mToneSequence.isToneLastInSequence();
        button.setText("Beep");
        enableButtons(false);
        //play for tone for its duration
        note.start();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    note.stop();
                    note.prepare();
                    button.setText(text);
                    if (isPlayerTurn) {
                        if (isLastInSequence)
                            enableButtons(false);
                        else
                            enableButtons(true);
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
                playTone(mBlueButton, mBlueNote, color.toString(), false);
                break;
            case RED:
                playTone(mRedButton, mRedNote, color.toString(), false);
                break;
            case YELLOW:
                playTone(mYellowButton, mYellowNote, color.toString(), false);
                break;
            case GREEN:
                playTone(mGreenButton, mGreenNote, color.toString(), false);
                break;
            default:
        }
    }

    // Adds a new tone to the sequence, plays each tone, then allows the
    // player to input the tones.
    private void computerPlayToneSequence() {
        //add a new tone to sequence
        mToneSequence.addRandomTone();

        mCountDownTimer = new CountDownTimer(30000, TONE_LENGTH + TONE_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                if (!mToneSequence.isSequenceFinished()) {
                    computerPlayTone(mToneSequence.getNextTone());
                }
                else {
                    this.cancel();
                    this.onFinish();
                }
            }

            public void onFinish() {
                if (!mToneSequence.isSequenceFinished()) {
                    this.start();
                }
                else {
                    mToneSequence.resetToneIndex();
                    enableButtons(true);
                }
            }
        }.start();
    }

    // Sets the phase of the game. There are two phases:
    // 1. The computer (CPU) plays tones for the player to memorize. Player input disabled.
    // 2. The player inputs the tones
    private void enableButtons(boolean isEnabled) {
        if (!isEnabled) {
            mBlueButton.setEnabled(false);
            mRedButton.setEnabled(false);
            mYellowButton.setEnabled(false);
            mGreenButton.setEnabled(false);
        } else {
            mBlueButton.setEnabled(true);
            mRedButton.setEnabled(true);
            mYellowButton.setEnabled(true);
            mGreenButton.setEnabled(true);
        }
    }

    private void checkCorrectInput(Color color) {
        //is the inputted tone correct?
        if (!mToneSequence.isInputCorrect(color)) {
            //player chose wrong. Wait for selected tone to finish, then play lose sound.
            new Handler().postDelayed(new Runnable() { public void run() {mLoseSound.start();}}, TONE_LENGTH);
            mMessageView.setText("Too bad. Better luck next time.");
            //Display 'play again' button
            mStartGameButton.setVisibility(View.VISIBLE);
            return;
        }
        //has the player won the round?
        if (mToneSequence.isSequenceFinished()) {
            //player inputted sequence correctly. Wait for selected tone to finish, then play victory sound.
            new Handler().postDelayed(new Runnable() { public void run() {mVictorySound.start();}}, TONE_LENGTH);
            enableButtons(false);
            mToneSequence.resetToneIndex();

            setScore(mToneSequence.getSize());
            if (mScore > mHighScoreManager.getHighScore(getContext(), mCurrentDifficulty)) {
                mHighScoreManager.setHighScore(getContext(), mCurrentDifficulty, mScore);
                String currentScore = ((Integer) mScore).toString();
                mHighScoreView.setText(currentScore);
            }
            //Wait some time, then play new tone sequence for player
            new Handler().postDelayed(new Runnable() { public void run() { computerPlayToneSequence();}}, 2 * TONE_LENGTH);
        }
    }

    private void setScore(Integer score) {
        mScore = score;
        String currentScore = score.toString();
        mScoreView.setText(currentScore);
    }


    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
        mBlueNote.stop();
        mRedNote.stop();
        mYellowNote.stop();
        mGreenNote.stop();
        mBlueNote.release();
        mRedNote.release();
        mYellowNote.release();
        mGreenNote.release();
        super.onDestroy();
    }
}
