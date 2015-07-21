package com.scheng.concurrency.report;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class ReportGenerator implements Callable<String> {

	private String sender, title;

	public ReportGenerator(String sender, String title) {
		this.sender = sender;
		this.title = title;
	}

	@Override
	public String call() throws Exception {
		try {
			Long duration = (long)(Math.random()*10);
			System.out.println(this.sender + "_" + this.title + ": ReportGenerator: Generating a report" +
					" during " + duration + " seconds.");
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String ret = sender + ": " + title;
		return ret;
	}

}