package com.scheng.java7;

/**
 * Created by scheng on 7/19/2015.
 */
import java.io.IOException;
import java.sql.SQLException;

public class MultiCatch {

    public static void main(String[] args) {
        try {
            doWork("what?");
        } catch (IOException|SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void doWork(String arg) throws IOException, SQLException {
        throw new SQLException("foo");
    }
}