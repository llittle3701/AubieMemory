package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

public class GameFragment extends Fragment {

    private final String ERROR_TAG = "error";
    private final String DEBUG_TAG = "debug";

    public enum Difficulty {BEGINNER, INTERMEDIATE, EXPERT}

    private int TONE_LENGTH = 1000; //tone length in milliseconds
    private int TONE_INTERVAL = 2000; //interval between each tone
    private static final Random RANDOM = new Random();

    ToneSequence mToneSequence;

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

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mScoreView = v.findViewById(R.id.score);
        mHighScoreView = v.findViewById(R.id.highscore);
        mMessageView = v.findViewById(R.id.message);

        String highScore = getHighScore().toString();
        mHighScoreView.setText(highScore);

        mBlueButton = v.findViewById(R.id.blue_button);
        mRedButton =  v.findViewById(R.id.red_button);
        mYellowButton = v.findViewById(R.id.yellow_button);
        mGreenButton = v.findViewById(R.id.green_button);
        mStartGameButton = v.findViewById(R.id.start_button);

        enableButtons(false);
        setDifficulty(getArguments());

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
                TONE_INTERVAL = 1000;
                break;
            case INTERMEDIATE:
                TONE_INTERVAL = 200;
                break;
            case EXPERT:
                TONE_INTERVAL = 50;
                break;
            default:
        }
    }

    private void playTone(final Button button, final MediaPlayer note,
                          final String text, final boolean isPlayerTurn) {

        final Boolean isLastInSequence = (mToneSequence.getCurrentToneIndex() == (mToneSequence.getSize() - 1));
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
         Color[] colors = Color.values();
        Color next = colors[RANDOM.nextInt(colors.length)];
        mToneSequence.addTone(next);

        new CountDownTimer(30000, TONE_LENGTH + TONE_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                if (mToneSequence.getCurrentToneIndex() < mToneSequence.getSize()) {
                    computerPlayTone(mToneSequence.getNextTone());
                }
                else {
                    this.cancel();
                    this.onFinish();
                }
            }

            public void onFinish() {
                if (mToneSequence.getCurrentToneIndex() < mToneSequence.getSize()) {
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
        if (color != mToneSequence.getNextTone()) {
            //player chose wrong. Wait for selected tone to finish, then play lose sound.
            new Handler().postDelayed(new Runnable() { public void run() {mLoseSound.start();}}, TONE_LENGTH);
            mToneSequence.clear();
            mToneSequence.resetToneIndex();
            mMessageView.setText("Too bad. Better luck next time.");
            mStartGameButton.setVisibility(View.VISIBLE);
            return;
        }
        //has the player won the round?
        if (mToneSequence.getCurrentToneIndex() == mToneSequence.getSize()) {
            //player inputted sequence correctly. Wait for selected tone to finish, then play victory sound.
            new Handler().postDelayed(new Runnable() { public void run() {mVictorySound.start();}}, TONE_LENGTH);
            enableButtons(false);
            mToneSequence.resetToneIndex();

            setScore(mToneSequence.getSize());
            if (mScore > getHighScore()) {
                setHighScore(mScore);
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

    private Integer getHighScore() {
        String rawXML = "";
        ArrayList<String> data = new ArrayList<>();
        try {
            FileInputStream fis = getContext().openFileInput(scoreFile);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            rawXML = new String(inputBuffer);
            isr.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            Log.e(ERROR_TAG, "FileNotFoundException in getHighScore : \n" + e.getMessage());
            return 0;
        }
        catch (IOException e) {
            Log.e(ERROR_TAG, "IOException in getHighScore: \n" + e.getMessage());
        }
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        }
        catch (XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        try {
            xpp.setInput(new StringReader(rawXML));
        }
        catch(XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        }
        catch (XmlPullParserException e) {
            Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
        }

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.d(DEBUG_TAG, "Start document");
            }
            else if (eventType == XmlPullParser.START_TAG) {
                Log.d(DEBUG_TAG, "Start tag: " + xpp.getName());
            }
            else if (eventType == XmlPullParser.END_TAG) {
                Log.d(DEBUG_TAG, "End tag: " + xpp.getName());
            }
            else if (eventType == XmlPullParser.TEXT) {
                data.add(xpp.getText());
            }
            try {
                eventType = xpp.next();
            }
            catch (XmlPullParserException e) {
                Log.e(ERROR_TAG, "XmlPullParserException in getHighScore: \n" + e.getMessage());
            }
            catch (IOException e) {
                Log.e(ERROR_TAG, "IOException in getHighScore: \n" + e.getMessage());
            }
        }

        String highScore = data.get(0);
        return Integer.parseInt(highScore);
    }

    private void setHighScore(Integer score) {
        try{
            FileOutputStream fos = getContext().openFileOutput(scoreFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "data");
            xmlSerializer.startTag(null, "highScore");
            xmlSerializer.text(score.toString());
            xmlSerializer.endTag(null, "highScore");
            xmlSerializer.endTag(null, "data");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fos.write(dataWrite.getBytes());
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.e(ERROR_TAG, "FileNotFoundException in setHighScore : \n" + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.e(ERROR_TAG, "IllegalArgumentException in setHighScore : \n" + e.getMessage());
        }
        catch (IllegalStateException e) {
            Log.e(ERROR_TAG, "IllegalStateException in setHighScore : \n" + e.getMessage());
        }
        catch (IOException e) {
            Log.e(ERROR_TAG, "IOException in setHighScore : \n" + e.getMessage());
        }
    }
}
