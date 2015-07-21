package com.scheng.concurrency.forkjoin;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;


public class SearchNumberTask extends RecursiveTask<Integer> {


	private static final long serialVersionUID = 1L;
	private int array[];
	private int start, end, number;
	private TaskManager manager;


	private final static int NOT_FOUND=-1;

	public SearchNumberTask(int array[], int start, int end, int number,
			TaskManager manager) {
		this.array =  array;
		this.start = start;
		this.end = end;
		this.number = number;
		this.manager = manager;
	}

	@Override
	protected Integer compute() {
		System.out.println("Task: " + start + ":" + end);
		int ret;
		if (end-start>10) {
			ret=launchTasks();
		} else {
			ret = lookForNumber();
		}
		return ret;
	}

	private int lookForNumber() {
		for (int i = start; i < end; i++) {
			if (array[i]==number) {
				System.out.printf("Task: Number %d found in position %d", number, i);
				manager.cancelTasks(this);
				return i;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return NOT_FOUND;
	}

	private int launchTasks() {
		int mid= (start+end)/2;

		SearchNumberTask task1 = new SearchNumberTask(array, start, mid, number, manager);
		SearchNumberTask task2 = new SearchNumberTask(array, mid, end, number, manager);

		manager.addTask(task1);
		manager.addTask(task2);

		task1.fork();
		task2.fork();

		int returnValue;

		returnValue = task1.join();
		if (returnValue!=-1) {
			return returnValue;
		}

		returnValue = task2.join();
		return returnValue;
	}

	public void writeCancelMessage() {
		System.out.printf("Task: Canceled task from %d to %d\n", start, end);
	}

    public static void main(String[] args) {

    		ArrayGenerator generator = new ArrayGenerator();
    		int array[] = generator.generateArray(1000);

    		TaskManager taskManager = new TaskManager();

    		ForkJoinPool pool = new ForkJoinPool();

    		SearchNumberTask task = new SearchNumberTask(array, 0, 1000, 5, taskManager);

    		pool.execute(task);

    		pool.shutdown();

    		try {
    			pool.awaitTermination(1, TimeUnit.DAYS);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		System.out.printf("Main: The program has finished.");
    	}

}