package com.scheng.java7;

/**
 * Created by scheng on 7/19/2015.
 */
public class Literals {
        //Java 6 - not easy to read
        public static int int6 = 12345678;
        public static long long6 = 12345678L;
        public static int bin6 = 0b0001001001001000;
        public static double double6 = 3.141592653589793d;

        //Java 7 - beautiful!
        public static int int7 = 12_345_678;
        public static long long7 = 1_2_3_4______5_6_7_8L;
        public static int bin7 = 0b0001_0010_0100_1000;
        public static double double7 = 3.141_592_653_589_793d;

    public static void main(String[] args) {
        System.out.println(int6 + long6 + bin6 + double6 + int7 + long7 + bin7 + double7);
    }
}