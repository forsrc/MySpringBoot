package com.forsrc.utils;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type My lock.
 */
public class MyLock {
    private ReentrantReadWriteLock lock;
    private Lock readLock;
    private Lock writeLock;

    /**
     * Instantiates a new My lock.
     */
    public MyLock() {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    /**
     * Todo read lock.
     *
     * @param todoReadLock the todo read lock
     * @throws Exception the exception
     */
    public void todoReadLock(TodoReadLock todoReadLock) throws Exception {
        this.readLock.lock();
        try {
            todoReadLock.todo();
        } catch (Exception e) {
            throw e;
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Todo write lock.
     *
     * @param todoWriteLock the todo write lock
     * @throws Exception the exception
     */
    public void todoWriteLock(TodoWriteLock todoWriteLock) throws Exception {
        this.writeLock.lock();
        try {
            todoWriteLock.todo();
        } catch (Exception e) {
            throw e;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Gets read lock.
     *
     * @return the read lock
     */
    public Lock getReadLock() {
        return readLock;
    }

    /**
     * Gets write lock.
     *
     * @return the write lock
     */
    public Lock getWriteLock() {
        return writeLock;
    }

    /**
     * The interface Todo read lock.
     */
    public static interface TodoReadLock {
        /**
         * Todo.
         *
         * @throws Exception the exception
         */
        public void todo() throws Exception;
    }

    /**
     * The interface Todo write lock.
     */
    public static interface TodoWriteLock {
        /**
         * Todo.
         *
         * @throws Exception the exception
         */
        public void todo() throws Exception;
    }

}
