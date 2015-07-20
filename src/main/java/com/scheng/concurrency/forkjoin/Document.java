package com.scheng.concurrency.forkjoin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scheng on 7/19/2015.
 */
public class Document {
    private final List<String> lines;
    	private final String name;

    	Document(List<String> lines, String name) {
    		this.lines = lines;
    		this.name = name;
    	}

    	List<String> getLines() {
    		return lines;
    	}

    	static Document fromFile(File file) throws IOException {
    		List<String> lines = new ArrayList<>();
    		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    			String line = reader.readLine();
    			while (line != null) {
    				lines.add(line);
    				line = reader.readLine();
    			}
    		}
    		return new Document(lines, file.getName());
    	}

    	@Override
    	public String toString() {
    		return "Document{" +
    				"name=" + name +
    				", lines=" + lines.size() +
    				'}';
    	}
}
