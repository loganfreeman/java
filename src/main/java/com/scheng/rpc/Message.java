package com.scheng.rpc;

import java.io.Serializable;

/**
 * Created by scheng on 7/22/2015.
 */
public class Message implements Serializable {
    private String m;

    public Message(String m) {
        this.m = m;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}