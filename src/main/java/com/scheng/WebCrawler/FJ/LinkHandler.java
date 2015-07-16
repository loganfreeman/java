package com.scheng.WebCrawler.FJ;

/**
 * Created by scheng on 7/16/2015.
 */
public interface LinkHandler {

    /**
     * Returns the number of visited links
     * @return
     */
    int size();

    /**
     * Checks if the link was already visited
     * @param link
     * @return
     */
    boolean visited(String link);

    /**
     * Marks this link as visited
     * @param link
     */
    void addVisited(String link);
}
