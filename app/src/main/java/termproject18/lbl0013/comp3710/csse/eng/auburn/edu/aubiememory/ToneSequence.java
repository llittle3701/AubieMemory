package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import java.util.ArrayList;
import java.util.Random;

public class ToneSequence {

    private ArrayList<Color> mToneSequence;
    private int mCurrentToneIndex;

    private static final Random RANDOM = new Random();

    public ToneSequence() {
        mToneSequence = new ArrayList<>();
        mCurrentToneIndex = 0;
    }

    public Color getNextTone() {
        Color color =  mToneSequence.get(mCurrentToneIndex);
        mCurrentToneIndex++;
        return color;
    }

    public void addRandomTone() {
        Color[] colors = Color.values();
        Color next = colors[RANDOM.nextInt(colors.length)];
        mToneSequence.add(next);
    }

    public int getSize() {
        return mToneSequence.size();
    }

    private int getCurrentToneIndex() {
        return mCurrentToneIndex;
    }

    public void resetToneIndex() {
        mCurrentToneIndex = 0;
    }

    public void clear() {
        mToneSequence.clear();
    }

    public boolean isInputCorrect(Color color) {
        if (color != getNextTone()) {
            clear();
            resetToneIndex();
            return false;
        }
        return true;
    }

    public boolean isSequenceFinished() {
        if (getCurrentToneIndex() >= getSize()) {
            return true;
        }
        return false;
    }

    public boolean isToneLastInSequence() {
        return (mCurrentToneIndex == (getSize() - 1));
    }
}
