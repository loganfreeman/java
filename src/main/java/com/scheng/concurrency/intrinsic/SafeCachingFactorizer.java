package com.scheng.concurrency.intrinsic;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by scheng on 7/19/2015.
 */
public class SafeCachingFactorizer {

    private final AtomicInteger lastNumber = new AtomicInteger();
    private final AtomicReference<ArrayList<Integer>> lastFactors = new AtomicReference<ArrayList<Integer>>();

    public static void main(String[] args){
        SafeCachingFactorizer ucf = new SafeCachingFactorizer();

        System.out.println(ucf.cacheFactor(4));
        System.out.println(ucf.cacheFactor(4));
        System.out.println(ucf.cacheFactor(40));
        System.out.println(ucf.cacheFactor(30));
        System.out.println(ucf.cacheFactor(3600));
    }

    public ArrayList<Integer> cacheFactor(Integer i) {
        if (i.equals(lastNumber.get())) {
            System.out.println("Fetching from Cache..for value "+i);
            return lastFactors.get();
        } else {
            System.out.println("Storing onto Cache..for value "+i);
            ArrayList<Integer> factors = getFactors(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            return lastFactors.get();
        }
    }

    private ArrayList<Integer> getFactors(Integer value){
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for(Integer factor=2;value>1;factor++){
           while(value%factor==0){
               factors.add(factor);
               value=value/factor;
           }
        }
        return factors;
    }
}