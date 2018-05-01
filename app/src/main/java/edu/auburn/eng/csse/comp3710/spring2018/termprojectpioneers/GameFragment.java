package edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers;

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
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.IOException;

import edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers.R;

public class GameFragment extends Fragment {

    private final String ERROR_TAG = "error";

    public enum Difficulty {BEGINNER, INTERMEDIATE, EXPERT}
    private String mCurrentDifficulty = "";

    private int TONE_LENGTH = 1000; //tone length in milliseconds
    private int TONE_INTERVAL = 2000; //interval between each tone

    //Helper classes
    ToneSequence mToneSequence;
    HighScoreManager mHighScoreManager;
    CountDownTimer mCountDownTimer;
    private int mScore;

    private ImageButton mBlueButton;
    private ImageButton mRedButton;
    private ImageButton mYellowButton;
    private ImageButton mGreenButton;
    private ImageButton mPinkButton;
    private ImageButton mLightBlueButton;
    private ImageButton mOrangeButton;
    private ImageButton mPurpleButton;
    private Button mStartGameButton;

    private MediaPlayer mBlueNote;
    private MediaPlayer mRedNote;
    private MediaPlayer mYellowNote;
    private MediaPlayer mGreenNote;
    private MediaPlayer mVictorySound;
    private MediaPlayer mLoseSound;

    private boolean mArePlayersReleased = false;
    private boolean isCPUPlaying = false;
    private boolean hasPlayerLost = false;

    private TextView mScoreView;
    private TextView mHighScoreView;
    private TextView mMessageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_game, container, false);

        mToneSequence = new ToneSequence();

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mScoreView = v.findViewById(R.id.score);
        mHighScoreView = v.findViewById(R.id.highscore);
        mMessageView = v.findViewById(R.id.message);

        mBlueButton = v.findViewById(R.id.blue_button);
        mRedButton =  v.findViewById(R.id.red_button);
        mYellowButton = v.findViewById(R.id.yellow_button);
        mGreenButton = v.findViewById(R.id.green_button);

        mPinkButton = v.findViewById(R.id.pink_button);
        mLightBlueButton = v.findViewById(R.id.lightblue_button);
        mOrangeButton = v.findViewById(R.id.orange_button);
        mPurpleButton = v.findViewById(R.id.purple_button);
        mStartGameButton = v.findViewById(R.id.start_button);

        setDifficulty(getArguments());

        //Set high score
        Context context = getContext();
        if (context != null)
            mHighScoreManager = new HighScoreManager(context);
        String highScore = mHighScoreManager.getHighScore(mCurrentDifficulty).toString();
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
                playTone(mBlueButton, mBlueNote,true);
                checkCorrectInput(Color.BLUE);
            }
        });

        mRedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mRedButton, mRedNote,true);
                checkCorrectInput(Color.RED);
            }
        });

        mYellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mYellowButton, mYellowNote,true);
                checkCorrectInput(Color.YELLOW);
            }
        });

        mGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mGreenButton, mGreenNote,true);
                checkCorrectInput(Color.GREEN);
            }
        });

        mPinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mPinkButton, mBlueNote,true);
                checkCorrectInput(Color.PINK);
            }
        });

        mLightBlueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mLightBlueButton, mRedNote,true);
                checkCorrectInput(Color.LIGHT_BLUE);
            }
        });

        mOrangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mOrangeButton, mYellowNote,true);
                checkCorrectInput(Color.ORANGE);
            }
        });

        mPurpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone(mPurpleButton, mGreenNote,true);
                checkCorrectInput(Color.PURPLE);
            }
        });

        mStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartGameButton.setVisibility(View.INVISIBLE);
                getFragmentManager().beginTransaction().detach(GameFragment.this).attach(GameFragment.this).commit();
            }
        });


        if (savedInstanceState != null) {
            mToneSequence = savedInstanceState.getParcelable("ToneSequence");
            mScore = savedInstanceState.getInt("Score");
            isCPUPlaying = savedInstanceState.getBoolean("isCPUPlaying");
            hasPlayerLost = savedInstanceState.getBoolean("hasPlayerLost");
            setScore(mScore);
            if (isCPUPlaying) {
                mMessageView.setText(R.string.watch_closely);
                enableButtons(false);
            }
            else {
                if (hasPlayerLost) {
                    enableButtons(false);
                    mMessageView.setText(R.string.too_bad);
                    //Display 'play again' button
                    mStartGameButton.setVisibility(View.VISIBLE);
                }
                else {
                    enableButtons(true);
                    mMessageView.setText(R.string.repeat_sequence);
                }
            }
        }
        else {

            mToneSequence = new ToneSequence();
            //Wait some time, then play tone sequence for player
            enableButtons(false);

            mMessageView.setText("");
            setScore(0);
            mToneSequence.resetToneIndex();
            mToneSequence.clear();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mToneSequence.addRandomTone();
                    computerPlayToneSequence();
                }
            }, 2 * TONE_LENGTH);
        }

        return v;
    }

    private void setDifficulty(Bundle bundle) {
        Difficulty difficulty = (Difficulty) bundle.get("Difficulty");
        switch (difficulty) {
            case BEGINNER:
                TONE_INTERVAL = 1000;
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

    private void playTone(final ImageButton button, final MediaPlayer note,
                            final boolean isPlayerTurn) {
        final Boolean isLastInSequence = mToneSequence.isToneLastInSequence();
        setSelected(button);
        enableButtons(false);
        //play for tone for its duration
        if (!mArePlayersReleased) {
            note.start();
        }
        else {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    if (!mArePlayersReleased) {
                        note.stop();
                        note.prepare();
                    }
                    unselect(button);
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


    private void setSelected(ImageButton button) {
        if (button == mBlueButton)
            mBlueButton.setBackgroundResource(R.drawable.blueaubie_tl);
        else if (button == mRedButton)
            mRedButton.setBackgroundResource(R.drawable.redaubie_tr);
        else if (button == mYellowButton)
            mYellowButton.setBackgroundResource(R.drawable.yellowaubie_bl);
        else if (button == mGreenButton)
            mGreenButton.setBackgroundResource(R.drawable.greenaubie_br);
        else if (button == mPinkButton)
            mPinkButton.setBackgroundResource(R.drawable.pinkaubie_tl);
        else if (button == mLightBlueButton)
            mLightBlueButton.setBackgroundResource(R.drawable.lightblueaubie_tr);
        else if (button == mOrangeButton)
            mOrangeButton.setBackgroundResource(R.drawable.orangeaubie_bl);
        else if (button == mPurpleButton)
            mPurpleButton.setBackgroundResource(R.drawable.purpleaubie_br);
    }

    private void unselect(ImageButton button) {
        if (button == mBlueButton)
            mBlueButton.setBackgroundResource(R.drawable.blue_tl);
        else if (button == mRedButton)
            mRedButton.setBackgroundResource(R.drawable.red_tr);
        else if (button == mYellowButton)
            mYellowButton.setBackgroundResource(R.drawable.yellow_bl);
        else if (button == mGreenButton)
            mGreenButton.setBackgroundResource(R.drawable.green_br);
        else if (button == mPinkButton)
            mPinkButton.setBackgroundResource(R.drawable.pink_tl);
        else if (button == mLightBlueButton)
            mLightBlueButton.setBackgroundResource(R.drawable.lightblue_tr);
        else if (button == mOrangeButton)
            mOrangeButton.setBackgroundResource(R.drawable.orange_bl);
        else if (button == mPurpleButton)
            mPurpleButton.setBackgroundResource(R.drawable.purple_br);
    }

    private void computerPlayTone(Color color) {
        switch (color) {
            case BLUE:
                playTone(mBlueButton, mBlueNote, false);
                break;
            case RED:
                playTone(mRedButton, mRedNote,false);
                break;
            case YELLOW:
                playTone(mYellowButton, mYellowNote, false);
                break;
            case GREEN:
                playTone(mGreenButton, mGreenNote, false);
                break;
            case PINK:
                playTone(mPinkButton, mBlueNote, false);
                break;
            case LIGHT_BLUE:
                playTone(mLightBlueButton, mRedNote, false);
                break;
            case ORANGE:
                playTone(mOrangeButton, mYellowNote, false);
                break;
            case PURPLE:
                playTone(mPurpleButton, mGreenNote, false);
                break;
            default:
        }
    }

    // Adds a new tone to the sequence, plays each tone, then allows the
    // player to input the tones.
    private void computerPlayToneSequence() {
        mMessageView.setText(R.string.watch_closely);
        isCPUPlaying = true;

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
                    isCPUPlaying = false;
                    enableButtons(true);
                    mMessageView.setText(R.string.repeat_sequence);
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
            mPinkButton.setEnabled(false);
            mLightBlueButton.setEnabled(false);
            mOrangeButton.setEnabled(false);
            mPurpleButton.setEnabled(false);
        } else {
            mBlueButton.setEnabled(true);
            mRedButton.setEnabled(true);
            mYellowButton.setEnabled(true);
            mGreenButton.setEnabled(true);
            mPinkButton.setEnabled(true);
            mLightBlueButton.setEnabled(true);
            mOrangeButton.setEnabled(true);
            mPurpleButton.setEnabled(true);
        }
    }

    private void checkCorrectInput(Color color) {
        //is the inputted tone correct?
        if (!mToneSequence.isInputCorrect(color)) {
            //player chose wrong. Wait for selected tone to finish, then play lose sound.
            new Handler().postDelayed(new Runnable() { public void run() {mLoseSound.start();}}, TONE_LENGTH);
            hasPlayerLost = true;
            enableButtons(false);
            mMessageView.setText(R.string.too_bad);
            //Display 'play again' button
            mStartGameButton.setVisibility(View.VISIBLE);
            return;
        }
        //has the player won the round?
        if (mToneSequence.isSequenceFinished()) {
            //player inputted sequence correctly. Wait for selected tone to finish, then play victory sound.
            hasPlayerLost = false;
            mMessageView.setText(R.string.well_done);
            new Handler().postDelayed(new Runnable() { public void run() {mVictorySound.start();}}, TONE_LENGTH);
            enableButtons(false);
            mToneSequence.resetToneIndex();

            setScore(mToneSequence.getSize());
            if (mScore > mHighScoreManager.getHighScore(mCurrentDifficulty)) {
                mHighScoreManager.setHighScore(mCurrentDifficulty, mScore);
                String currentScore = ((Integer) mScore).toString();
                mHighScoreView.setText(currentScore);
            }
            //Wait some time, then play new tone sequence for player
            new Handler().postDelayed(new Runnable() { public void run() {
                mToneSequence.addRandomTone();
                computerPlayToneSequence();}}, 2 * TONE_LENGTH);
        }
    }

    private void setScore(Integer score) {
        mScore = score;
        String currentScore = score.toString();
        mScoreView.setText(currentScore);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("ToneSequence", mToneSequence);
        savedInstanceState.putInt("Score", mScore);
        savedInstanceState.putBoolean("isCPUPlaying", isCPUPlaying);
        savedInstanceState.putBoolean("hasPlayerLost", hasPlayerLost);
    }

    @Override
    public void onPause() {
        if (isCPUPlaying) {
            mCountDownTimer.cancel();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        //Wait 3 seconds, then resume the sequence
        if (isCPUPlaying) {
            new Handler().postDelayed(new Runnable() { public void run() {
                computerPlayToneSequence();}
                }, 3000);
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mBlueNote.stop();
        mRedNote.stop();
        mYellowNote.stop();
        mGreenNote.stop();
        mBlueNote.release();
        mRedNote.release();
        mYellowNote.release();
        mGreenNote.release();
        mArePlayersReleased = true;
        super.onDestroy();
    }



}
