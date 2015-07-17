package com.scheng.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Created by scheng on 7/16/2015.
 */
public class ProductTask extends RecursiveAction {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 产品列表
    private List<Product> products;

    // 第一个和最后一个间隔分配到任务
    private int first;
    private int last;

    //
    private double increment;

    public ProductTask(List<Product> products, int first, int last, double increment) {
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    @Override
    protected void compute() {
        if (last - first < 10) {
            updatePrices();
        } else {
            int middle = (last + first) / 2;
            System.out.printf("Task: Pending tasks: %s\n", getQueuedTaskCount());
            ProductTask t1 = new ProductTask(products, first, middle + 1, increment);
            ProductTask t2 = new ProductTask(products, middle + 1, last, increment);
            System.out.println("t1 : " + t1);
            System.out.println("t2 : " + t2);
            System.out.println();
            invokeAll(t1, t2);
        }
    }

    private void updatePrices() {
        System.out.println("first : " + this.first + "  last : " + last);
        System.out.println();
        for (int i = first; i < last; i++) {
            Product product = products.get(i);
            product.setPrice(product.getPrice() * (1 + increment));
        }
    }

    @Override
    public String toString() {
        return "Task [first=" + first + ", last=" + last + "]";
    }

    public static List<Product> generate(int size) {
        List<Product> ret = new ArrayList<Product>();

        for (int i = 0; i < size; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setPrice(10);
            ret.add(product);
        }

        return ret;
    }

    public static void main(String[] args) {
    		// 产品列表
    		List<Product> products = generate(40);

    		// 创建一个任务
    		ProductTask task = new ProductTask(products, 0, products.size(), 0.20);

    		// 创建一个ForJoinPool
    		ForkJoinPool pool = new ForkJoinPool();

    		// 执行任务
    		pool.execute(task);

    		// 输出pool的信息
    		do {
    			System.out.printf("Main: Thread Count: %d\n", pool.getActiveThreadCount());
    			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount());
    			System.out.printf("Main: Paralelism: %d\n", pool.getParallelism());

    			try {
    				TimeUnit.MILLISECONDS.sleep(5);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		} while (!task.isDone());

    		// 关闭
    		pool.shutdown();

    		// 检查任务是否正常完成
    		if (task.isCompletedNormally()) {
    			System.out.printf("Main: The process has completed normally.\n");
    		}

    		for (int i = 0; i < products.size(); i++) {
    			Product product = products.get(i);
    			if (product.getPrice() != 12) {
    				System.out.printf("Product %s: %f\n", product.getName(), product.getPrice());
    			}
    		}

    		System.out.println("Main: End of the program.\n");
    	}
}