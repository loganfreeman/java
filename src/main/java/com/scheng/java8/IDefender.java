package com.scheng.java8;

/**
 * Created by scheng on 7/20/2015.
 */
public interface IDefender {

    public void likeIt(String s);


    public default void doIt() {
        System.out.println("doIt function..");
    }


}