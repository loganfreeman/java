package com.scheng.concurrency.report;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class ReportProcessor implements Runnable {

	private CompletionService<String> service;
	private boolean end;

	public ReportProcessor(CompletionService<String> service) {
		this.service = service;
		end = false;
	}

	@Override
	public void run() {
		while (!end) {
			try {
				Future<String> result = service.poll(20, TimeUnit.SECONDS);
				if (result!=null) {
					String report = result.get();
					System.out.println("ReportReceiver: Report Received: " + report);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("ReportSender: End");
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

}