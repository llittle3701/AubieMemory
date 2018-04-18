package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreManager {

    public static final String PREFS_NAME = "myPreferences";
    private final String ERROR_TAG = "error";
    private final String DEBUG_TAG = "debug";
    private SharedPreferences mSharedPreferences;

    public HighScoreManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public Integer getHighScore(Context context, String key) {
        Integer score = mSharedPreferences.getInt(key, 0);
        return score;
    }

    public void setHighScore(Context context, String key, Integer score) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, score);
        editor.apply();
    }

}
