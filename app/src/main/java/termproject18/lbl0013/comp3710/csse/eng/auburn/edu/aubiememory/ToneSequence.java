package termproject18.lbl0013.comp3710.csse.eng.auburn.edu.aubiememory;

import java.util.ArrayList;

public class ToneSequence {

    private ArrayList<Color> mToneSequence;
    private int mCurrentToneIndex;

    public ToneSequence() {
        mToneSequence = new ArrayList<>();
        mCurrentToneIndex = 0;
    }

    public Color getNextTone() {
        Color color =  mToneSequence.get(mCurrentToneIndex);
        mCurrentToneIndex++;
        return color;
    }

    public void addTone(Color color) {
        mToneSequence.add(color);
    }

    public int getSize() {
        return mToneSequence.size();
    }

    public int getCurrentToneIndex() {
        return mCurrentToneIndex;
    }

    public void resetToneIndex() {
        mCurrentToneIndex = 0;
    }

    public void clear() {
        mToneSequence.clear();
    }
}
