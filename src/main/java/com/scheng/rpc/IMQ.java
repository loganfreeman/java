package com.scheng.rpc;

/**
 * Created by scheng on 7/22/2015.
 */
public interface IMQ {
    public Boolean sendMessage(Message m) throws IllegalStateException;
    public Message recivedMessage() throws InterruptedException;
}