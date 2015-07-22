package com.scheng.rpc;

/**
 * Created by scheng on 7/22/2015.
 */
public class Threads {
    public static void sleep(Long time){
                   try {
                       Thread.sleep(time);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
}
