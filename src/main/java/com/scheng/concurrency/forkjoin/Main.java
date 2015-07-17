package com.scheng.concurrency.forkjoin;

/**
 * Created by scheng on 7/16/2015.
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Main class of the example. It creates all the elements for the
 * execution and writes information about the Fork/Join pool that
 * executes the task
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * Create the Fork/Join pool
		 */
		ForkJoinPool pool=new ForkJoinPool();

		/*
		 * Create a new Array of integer numbers with 10000 elements
		 */
		int array[]=new int[10000];

		/*
		 * Create a new task
		 */
		Task task1=new Task(array,0,array.length);

		/*
		 * Execute the task in the Fork/Join pool
		 */
		pool.execute(task1);

		/*
		 * Wait for the finalization of the task writing information
		 * about the pool every second
		 */
		while (!task1.isDone()) {
			showLog(pool);
			TimeUnit.SECONDS.sleep(1);
		}

		/*
		 * Shutdown the pool
		 */
		pool.shutdown();

		/*
		 * Wait for the finalization of the pool
		 */
		pool.awaitTermination(1, TimeUnit.DAYS);

		/*
		 * End of the program
		 */
		showLog(pool);
		System.out.printf("Main: End of the program.\n");

	}

	/*
	 * This method writes information about a Fork/Join pool
	 */
	private static void showLog(ForkJoinPool pool) {
		System.out.printf("**********************\n");
		System.out.printf("Main: Fork/Join Pool log\n");
		System.out.printf("Main: Fork/Join Pool: Parallelism: %d\n",pool.getParallelism());
		System.out.printf("Main: Fork/Join Pool: Pool Size: %d\n",pool.getPoolSize());
		System.out.printf("Main: Fork/Join Pool: Active Thread Count: %d\n",pool.getActiveThreadCount());
		System.out.printf("Main: Fork/Join Pool: Running Thread Count: %d\n",pool.getRunningThreadCount());
		System.out.printf("Main: Fork/Join Pool: Queued Submission: %d\n",pool.getQueuedSubmissionCount());
		System.out.printf("Main: Fork/Join Pool: Queued Tasks: %d\n",pool.getQueuedTaskCount());
		System.out.printf("Main: Fork/Join Pool: Queued Submissions: %s\n",pool.hasQueuedSubmissions());
		System.out.printf("Main: Fork/Join Pool: Steal Count: %d\n",pool.getStealCount());
		System.out.printf("Main: Fork/Join Pool: Terminated : %s\n",pool.isTerminated());
		System.out.printf("**********************\n");
	}

    private static class Task extends RecursiveAction {

    	/**
    	 * Declare and initialize the serial version uid of the class
    	 */
    	private static final long serialVersionUID = 1L;

    	/**
    	 * Array of elements you want to increment
    	 */
    	private int array[];

    	/**
    	 * Start position of the set of elements this task has to process
    	 */
    	private int start;

    	/**
    	 * End position of the set of elements this task has to process
    	 */
    	private int end;

    	/**
    	 * Constructor of the class. Initializes its attributes
    	 *
    	 * @param array
    	 *            Array of elements this task has to process
    	 * @param start
    	 *            Start position of the set of elements this task has to process
    	 * @param end
    	 *            End position of the set of elements this task has to process
    	 */
    	public Task(int array[], int start, int end) {
    		this.array = array;
    		this.start = start;
    		this.end = end;
    	}

    	/**
    	 * Main method of the task. If it has to process more that 100 elements, it
    	 * divides that set it two sub-sets and creates two task to process them.
    	 * Otherwise, it increments directly the elements it has to process.
    	 */
    	@Override
    	protected void compute() {
    		if (end - start > 100) {
    			int mid = (start + end) / 2;
    			Task task1 = new Task(array, start, mid);
    			Task task2 = new Task(array, mid, end);

    			/*
    			 * Start the sub-tasks
    			 */
    			task1.fork();
    			task2.fork();

    			/*
    			 * Wait for the finalization of the sub-tasks
    			 */
    			task1.join();
    			task2.join();
    		} else {
    			for (int i = start; i < end; i++) {
    				array[i]++;

    				try {
    					TimeUnit.MILLISECONDS.sleep(5);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}

    }
}