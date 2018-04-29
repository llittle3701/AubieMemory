package edu.auburn.eng.csse.comp3710.spring2018.termprojectpioneers;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

public class ToneSequence implements Parcelable {

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

    protected ToneSequence(Parcel in) {
        if (in.readByte() == 0x01) {
            mToneSequence = new ArrayList<Color>();
            in.readList(mToneSequence, Color.class.getClassLoader());
        } else {
            mToneSequence = null;
        }
        mCurrentToneIndex = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mToneSequence == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mToneSequence);
        }
        dest.writeInt(mCurrentToneIndex);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ToneSequence> CREATOR = new Parcelable.Creator<ToneSequence>() {
        @Override
        public ToneSequence createFromParcel(Parcel in) {
            return new ToneSequence(in);
        }

        @Override
        public ToneSequence[] newArray(int size) {
            return new ToneSequence[size];
        }
    };
}

