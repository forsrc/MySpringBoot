package com.forsrc.boot.websocket.user;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ClientEndpoint
public class UserClientEndpoint extends Endpoint {

    private static final CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock LOCK_READ = LOCK.readLock();
    private static final Lock LOCK_WRITE = LOCK.writeLock();
    private static final ThreadLocal<Session> THREAD_LOCAL = new ThreadLocal<>();
    private static long count;

    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        setCount(1);
        System.out.println(String.format("--> ClientEndpoint onOpen() --> %s", session));
        System.out.println(String.format("--> ClientEndpoint onOpen() --> count: %s", getCount()));
        THREAD_LOCAL.set(session);
    }

    public void onOpen(Session session, EndpointConfig ec) {
        sessionSet.add(session);
        setCount(1);
        System.out.println(String.format("--> ClientEndpoint onOpen() --> %s", session));
        System.out.println(String.format("--> ClientEndpoint onOpen() --> count: %s", getCount()));
        THREAD_LOCAL.set(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(String.format("--> ClientEndpoint onMessage() --> %s", session));
        System.out.println(String.format("--> ClientEndpoint onMessage() --> message: %s", message));
        if (message != null && message.equals(String.format("CLOSE: %s", session.getId()))) {
            close(session);
        }
    }

    @OnClose
    public void onClose() {
        System.out.println(String.format("--> ClientEndpoint onClose() --> %s", getCount()));
        Session session = THREAD_LOCAL.get();
        close(session);
        sessionSet.remove(session);
        setCount(-1);
        THREAD_LOCAL.remove();
    }

    private void close(Session session) {
        if (session != null && session.isOpen()) {
            synchronized (session) {
                if (session != null && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(String.format("CLOSE: %s", session.getId()));
                    } catch (IOException ex) {
                        Logger.getLogger(UserClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println(String.format("--> ClientEndpoint onError() --> %s", error.getMessage()));
        error.printStackTrace();
    }

    public long getCount() {
        LOCK_READ.lock();
        if (LOCK_READ.tryLock()) {
            try {
                return count;
            } finally {
                LOCK_READ.unlock();
            }
        }
        return -1;
    }

    public void setCount(int n) {
        LOCK_WRITE.lock();
        if (LOCK_WRITE.tryLock()) {
            try {
                count += n;
            } finally {
                LOCK_WRITE.unlock();
            }
        }
    }
}
