package com.scheng.java7;

/**
 * Created by scheng on 7/19/2015.
 */
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class NIO2FileSearch {

    public static void main(String[] args) throws Exception {

        Files.walkFileTree(Paths.get("C:/users/scheng/projects/java"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) {
                if (path.endsWith("NIO2FileSearch.java")) {
                    System.out.println("Found It!!");
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}