package com.scheng.concurrency.activeobject;

/**
 * Created by scheng on 7/17/2015.
 */
import java.util.concurrent.Future;

public interface SampleActiveObject {
	public Future<String> process(String arg, int i);
}