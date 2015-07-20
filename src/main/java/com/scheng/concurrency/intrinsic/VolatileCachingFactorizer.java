package com.scheng.concurrency.intrinsic;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by scheng on 7/19/2015.
 */
public class VolatileCachingFactorizer {

    private volatile OneValueCache ovc = new OneValueCache(1, new Integer[]{1});

    public static void main(String[] args) {
        VolatileCachingFactorizer vcf = new VolatileCachingFactorizer();

        vcf.cacheFactor(4);
        vcf.cacheFactor(40);
        vcf.cacheFactor(30);
        vcf.cacheFactor(30);
        vcf.cacheFactor(3600);
    }

    public void cacheFactor(Integer i) {
        if (i.equals(ovc.getLastNumber())) {
            System.out.println("Fetching from Cache..for value " + i);
        } else {
            System.out.println("Storing onto Cache..for value " + i);
            Integer[] factors = getFactors(i);
            ovc = new OneValueCache(i, factors);
        }
        ovc.printLastFactors();
    }

    private Integer[] getFactors(Integer value) {        
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for (Integer factor = 2; value > 1; factor++) {
            while (value % factor == 0) {
                factors.add(factor);
                value = value / factor;
            }
        }        
        return factors.toArray(new Integer[factors.size()]);
    }

    private class OneValueCache {
        private final Integer lastNumber;
            private final Integer[] lastFactors;

            public OneValueCache(Integer i,
                    Integer[] factors) {
                lastNumber = i;
                lastFactors = Arrays.copyOf(factors, factors.length);
            }

            public Integer getLastNumber() {
                return lastNumber;
            }

            public Integer[] getLastFactors() {
                return lastFactors;
            }

            public void printLastFactors(){
                System.out.print("Factors are ->");
                for(int i=0;i<lastFactors.length;i++){
                    System.out.print(lastFactors[i]+" ");
                }
                System.out.println("");
            }

            public Integer[] getFactors(Integer i) {
                if (lastNumber == null || !lastNumber.equals(i)) {
                    return null;
                } else {
                    return Arrays.copyOf(lastFactors, lastFactors.length);
                }
            }
    }
}
