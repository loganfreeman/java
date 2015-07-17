package com.scheng.concurrency.forkjoin;

/**
 * Created by scheng on 7/16/2015.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class FolderProcessor extends RecursiveTask<List<String>> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String path;

	// 要查找的文件的扩展名
	private String extension;

	public FolderProcessor(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}

	@Override
	protected List<String> compute() {
		List<String> list = new ArrayList<String>(); // 保存结果, 文件的全路径
		List<FolderProcessor> tasks = new ArrayList<FolderProcessor>(); // 保存将要处理子目录的子任务

		File file = new File(path);
		File[] content = file.listFiles();
		if (content != null) {
			for (int i = 0; i < content.length; i++) {
				if (content[i].isDirectory()) {
					// 如果是任务,执行一个新的任务
					FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();	// 异步执行
					tasks.add(task);
				} else {
					// 如果是文件,比较文件的扩展名和要查找的扩展名是否一致
					if (checkFile(content[i].getName())) {
						list.add(content[i].getAbsolutePath());
					}
				}
			}

			// 子任务超过50个输出信息
			if (tasks.size() > 50) {
				System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), tasks.size());
			}

			// 等待任务结束,处理结果
			addResultsFromTasks(list, tasks);
		}

		return list;
	}

	/**
	 * 调用join()方法等待执行结束,join()方法将返回任务的结果.
	 */
	private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
		for (FolderProcessor item : tasks) {
			list.addAll(item.join());
		}
	}

	private boolean checkFile(String name) {
		if (name.endsWith(extension)) {
			return true;
		}
		return false;
	}

    public static void main(String[] args) {
    		ForkJoinPool pool = new ForkJoinPool();

    		FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
    		FolderProcessor apps = new FolderProcessor("C:\\Program Files", "log");
    		FolderProcessor documents = new FolderProcessor("C:\\Documents And Settings", "log");

    		pool.execute(system);
    		pool.execute(apps);
    		pool.execute(documents);

    		do {
    			System.out.printf("******************************************\n");
    			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
    			System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
    			System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
    			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
    			System.out.printf("******************************************\n");
    			try {
    				TimeUnit.SECONDS.sleep(1);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		} while ((!system.isDone()) || (!apps.isDone()) || (!documents.isDone()));

    		pool.shutdown();

    		List<String> results;

    		results = system.join();
    		System.out.printf("System: %d files found.\n", results.size());

    		results = apps.join();
    		System.out.printf("Apps: %d files found.\n", results.size());

    		results = documents.join();
    		System.out.printf("Documents: %d files found.\n", results.size());

    	}

}