package com.scheng.rpc;
import java.io.Serializable;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by scheng on 7/22/2015.
 */
public class MQ implements IMQ, Serializable{
    private final static BlockingDeque<Message> qu = new LinkedBlockingDeque<Message>(10);
    private final static long timeout = 2L;
    private final static TimeUnit unit  = TimeUnit.SECONDS;

    /**
     * 满了的时候，就抛出异常
     * @param m
     * @throws IllegalStateException
     */
    public Boolean sendMessage(Message m){
        return qu.offerFirst(m);
    }

    /**
     * 超时返回null
     * @return
     * @throws InterruptedException
     */
    public Message recivedMessage() throws InterruptedException {
        return qu.pollLast(timeout,unit); //qu.pollLast();会产生饥饿
    }

    public static void main(String[] args) throws Exception {
        IMQ mq = new MQ();
        RpcFramework.export(mq,1234);
    }
}