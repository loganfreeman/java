package com.scheng;

import junit.framework.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scheng on 10/29/2014.
 */
public class TwitterHandleTest extends TestCase {

    public void testTwitterHandle() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(AppTest.class.getResourceAsStream("/WordList.txt")));

        String strLine;

        List<String> words = new ArrayList<String>();

        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            // System.out.println(strLine);
            if(strLine.startsWith("at") && !strLine.contains("'")){
                words.add(strLine.replaceAll("at", "@"));
            }

        }

        Collections.sort(words, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });

        System.out.println("Short handle");
        for(int i = 0; i < 20; i++){
            System.out.println(words.get(i) + ":" + words.get(i).replaceAll("@", "at"));
        }

        System.out.println("Long handle");

        for(int i = 0; i < 20; i++){
            int j = words.size() - 1 - i;
            System.out.println(words.get(j) + ":" + words.get(j).replaceAll("@", "at"));
        }

        //Close the input stream
        br.close();

    }
}
