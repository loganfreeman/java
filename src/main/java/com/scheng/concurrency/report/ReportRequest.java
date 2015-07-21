package com.scheng.concurrency.report;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.concurrent.*;


public class ReportRequest implements Runnable {

	private String name;
	private CompletionService<String> service;

	public ReportRequest(String name, CompletionService<String> service) {
		this.name = name;
		this.service = service;
	}

	@Override
	public void run() {
		ReportGenerator reportGenerator = new ReportGenerator(name, "Report");
		service.submit(reportGenerator);
	}

    public static void main(String[] args) {

    		ExecutorService executor = (ExecutorService) Executors.newCachedThreadPool();

    		CompletionService<String> service = new ExecutorCompletionService<String>(executor);

    		ReportRequest faceRequest = new ReportRequest("Face", service);
    		ReportRequest onlineRequest = new ReportRequest("Online", service);

    		Thread faceThread = new Thread(faceRequest);
    		Thread onlineThread = new Thread(onlineRequest);

    		ReportProcessor processor = new ReportProcessor(service);
    		Thread senderThread = new Thread(processor);

    		System.out.println("Main: Starting The Threads");
    		faceThread.start();
    		onlineThread.start();
    		senderThread.start();

    		try {
    			System.out.println("Main: Waiting for the report generators.");
    			faceThread.join();
    			onlineThread.join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}

    		System.out.println("Main: Shutting down the executor.");
    		executor.shutdown();

    		try {
    			executor.awaitTermination(1, TimeUnit.DAYS);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}

    		processor.setEnd(true);
    		System.out.println("Main: Ends.");

    	}

}
