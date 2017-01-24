package com.forsrc.utils;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type My thread.
 */
public class MyThread {

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * Instantiates a new My thread.
     */
    public MyThread() {
        this(2, 3, 100, TimeUnit.MILLISECONDS, 3);
    }

    /**
     * Instantiates a new My thread.
     *
     * @param corePoolSize    the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime   the keep alive time
     * @param unit            the unit
     * @param capacity        the capacity
     */
    public MyThread(int corePoolSize,
                    int maximumPoolSize,
                    long keepAliveTime,
                    TimeUnit unit,
                    int capacity) {
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, new ArrayBlockingQueue<Runnable>(capacity),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * Submit future.
     *
     * @param <T>  the type parameter
     * @param task the task
     * @return the future
     */
    public <T> Future<T> submit(Runnable task) {
        return (Future<T>) this.threadPoolExecutor.submit(task);
    }

    /**
     * Submit future.
     *
     * @param <T>    the type parameter
     * @param task   the task
     * @param result the result
     * @return the future
     */
    public <T> Future<T> submit(Runnable task, T result) {
        return (Future<T>) this.threadPoolExecutor.submit(task);
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        if (this.threadPoolExecutor == null) {
            return;
        }
        this.threadPoolExecutor.shutdown();
        try {
            while (!this.threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.threadPoolExecutor.shutdownNow();
            this.threadPoolExecutor = null;
        }
    }

    /**
     * Shutdown now.
     */
    public void shutdownNow() {
        if (this.threadPoolExecutor == null) {
            return;
        }
        this.threadPoolExecutor.shutdownNow();
        this.threadPoolExecutor = null;
    }

    /**
     * Gets thread pool executor.
     *
     * @return the thread pool executor
     */
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }

    /**
     * Sets thread pool executor.
     *
     * @param threadPoolExecutor the thread pool executor
     */
    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}
