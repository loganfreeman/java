package com.scheng.rpc;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by scheng on 7/22/2015.
 */
public class Cusumer {

    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void produce(){
        //提交两个任务给线程池
        pool.submit(new Task());
        pool.submit(new Task());
    }

    static class Task implements Runnable {

        private Boolean isStop = false;
        private Message m = null;
        private IMQ mq;

        public Task(){
            try {
                mq = RpcFramework.refer(IMQ.class, "127.0.0.1", 1234);
            } catch (Exception e) {
                System.out.print("Rpc Exception Happened!");
                this.isStop = true;
            }
        }

        @Override
        public void run() {
            while (!isStop && m == null){
                try {
                    m = mq.recivedMessage();

                    if(m != null){
                        process();
                    }else {
                        System.out.println(Thread.currentThread().getName() + "mq is empty...again!");
                        Threads.sleep(2 * 1000L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName() + "mq is InterruptedException...");
                    isStop = true;
                }
            }
        }

        public void process(){
            System.out.println("【" + Thread.currentThread().getName() +"】get message from mq --> " + m.getM());
            m = null;
        }
    }

    public static void main(String[] args){
        Cusumer.produce();
    }


}