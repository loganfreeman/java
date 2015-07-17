package com.scheng.concurrency.forkjoin;

/**
 * Created by scheng on 7/16/2015.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class DocumentTask extends RecursiveTask<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 要处理的文档
    private String[][] document;

    // 需要处理的文档的行范围
    private int start, end;

    // 要查找的关键字
    private String word;

    public DocumentTask(String[][] document, int start, int end, String word) {
        this.document = document;
        this.start = start;
        this.end = end;
        this.word = word;
    }

    @Override
    protected Integer compute() {
        Integer result = null;
        if (end - start < 10) {
            // 小于10行, 直接处理
            result = processLines(document, start, end, word);
        } else {
            // 分隔任务
            int mid = (start + end) / 2;
            DocumentTask task1 = new DocumentTask(document, start, mid, word);
            DocumentTask task2 = new DocumentTask(document, mid, end, word);

            // 处理任务
            invokeAll(task1, task2);

            try {
                // 计算结果
                result = groupResults(task1.get(), task2.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    // 处理行 => 行任务
    private Integer processLines(String[][] document, int start, int end, String word) {
        List<LineTask> tasks = new ArrayList<>();

        for (int i = start; i < end; i++) {
            // 行任务
            LineTask task = new LineTask(document[i], 0, document[i].length, word);
            tasks.add(task);
        }

        invokeAll(tasks);

        int result = 0;
        // 处理返回结果
        for (int i = 0; i < tasks.size(); i++) {
            LineTask task = tasks.get(i);

            try {
                result = result + task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return new Integer(result);
    }

    // 计算结果
    private Integer groupResults(Integer number1, Integer number2) {
        Integer result;
        result = number1 + number2;

        return result;
    }

    // 定义一些关键词, 用来生成String矩阵
    private static String words[] = {"the", "hello", "goodbye", "packt", "java",
            "thread", "pool", "random", "class", "main"};

    // 生成二维数组,并计算关键字出现的次数
    public static String[][] generateDocument(int numLines, int numWords, String word) {
        int counter = 0;
        String[][] document = new String[numLines][numWords];
        Random random = new Random();
        for (int i = 0; i < numLines; i++) {
            for (int j = 0; j < numWords; j++) {
                int index = random.nextInt(words.length);
                document[i][j] = words[index];
                if (document[i][j].equals(word)) {
                    counter++;
                }
            }
        }

        System.out.printf("DocumentMock: The word appears %d times in the document.\n", counter);
        return document;
    }


    public static void main(String[] args) {
        // 生成一个10行和每行有100个单词的文档,书中是100行和每行1000个单词,这里是为了更快的看到结果
        String[][] document = generateDocument(10, 100, "the");

        DocumentTask task = new DocumentTask(document, 0, 10, "the");
        ForkJoinPool pool = new ForkJoinPool();

        pool.execute(task);

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

        } while (!task.isDone());

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.printf("Main: The word appears %d in the document", task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}