package com.scheng.concurrency.performance;

/**
 * Created by scheng on 7/21/2015.
 */
public interface Spaceship
{
    /**
     * Read the position of the spaceship into the array of coordinates provided.
     *
     * @param coordinates into which the x and y coordinates should be read.
     * @return the number of attempts made to read the current state.
     */
    int readPosition(final int[] coordinates);

    /**
     * Move the position of the spaceship by a delta to the x and y coordinates.
     *
     * @param xDelta delta by which the spaceship should be moved in the x-axis.
     * @param yDelta delta by which the spaceship should be moved in the y-axis.
     * @return the number of attempts made to write the new coordinates.
     */
    int move(final int xDelta, final int yDelta);
}