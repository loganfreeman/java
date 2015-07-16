package com.scheng;

/**
 * Created by scheng on 7/16/2015.
 */
import java.util.concurrent.RecursiveAction;

public class ForkMax extends RecursiveAction {

    final static int THRESHOLD = 10000;

    int[] mNumbers;
    int mMax, mStart, mLength;

    public ForkMax(int[] numbers, int start, int length){
        mNumbers = numbers;
        mStart = start;
        mLength = length;
    }

    protected void computeDirectly(){
        mMax = mNumbers[mStart];
        for(int i = mStart; i < mStart + mLength; i++)
            if(mMax < mNumbers[i])
                mMax = mNumbers[i];
    }

    @Override
    protected void compute() {

        if (mLength < THRESHOLD) {
            computeDirectly();
            return;
        }

        int split = mLength / 2;

        ForkMax forkMax1 = new ForkMax(mNumbers, mStart, split);
        ForkMax forkMax2 = new ForkMax(mNumbers, mStart + split, mLength - split);

        invokeAll( forkMax1, forkMax2 );

        forkMax1.join();
        forkMax2.join();

        mMax = forkMax1.mMax >= forkMax2.mMax ? forkMax1.mMax : forkMax2.mMax;

    }
}