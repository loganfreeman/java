package com.scheng.concurrency.net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by scheng on 7/19/2015.
 */
public class TaskExecutionWebServer{
    public static void main(String[] args) throws IOException{
        Executor executor = Executors.newFixedThreadPool(100);
        ServerSocket socket = new ServerSocket(80);
        while(true){
            final Socket remote = socket.accept();
            Runnable runnable = new Runnable() {
                public void run() {
                    handleRequest(remote);
                }
            };
            executor.execute(runnable);
        }
    }

    static void handleRequest(Socket remote){
        try{
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
            remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());
        String str = in.readLine();
        String sub = str.substring(str.indexOf("value=")+6, str.length()-9);
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Server: Bot");
        // this blank line signals the end of the headers
        out.println("");
        // Send the HTML page
        out.println("<H1>VIJAY</H2>");
        out.flush();
        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }

    }
}