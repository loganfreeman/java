package com.scheng.java8;

/**
 * Created by scheng on 7/20/2015.
 */
public interface IExtendedDefender extends IDefender {

    public default void doIt() {
        System.out.println("my extended doIt function..");
    }

    public static void main(String[] args) {

            IExtendedDefender def = (s) -> {
                System.out.println("likeIt method inherited from IDefender: " + s);
            };
            def.doIt();
            def.likeIt("I like the IExtended..");
        }
}