package com.scheng.concurrency.VideoConference;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.concurrent.CountDownLatch;


public class VideoConference implements Runnable {

	private final CountDownLatch controller;

	public VideoConference (int number){
		controller = new CountDownLatch(number);
	}

	public void arrive(String name){
		System.out.printf("%s has arrived.", name);
		controller.countDown();
		System.out.printf("VideoConference: Waiting for %d participants.\n", controller.getCount());
	}

	@Override
	public void run() {
		System.out.printf("VideoConference: Initialization: %d participants.\n", controller.getCount());

		try {
			controller.await();
			System.out.printf("VideoConference: Lets start...\n");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {

    		VideoConference videoConference = new VideoConference(10);

    		Thread threadConference = new Thread(videoConference);

    		threadConference.start();

    		for (int i = 0; i < 10; i++) {
    			Participant participant = new Participant(videoConference, "Participant " + i);
    			Thread thread = new Thread(participant);
    			thread.start();
    		}
    	}


}