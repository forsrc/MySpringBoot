package com.forsrc.boot.websocket.user;

import com.forsrc.boot.config.WebsocketConfig;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ServerEndpoint(value = "/wss/user", configurator = WebsocketConfig.ServerEndpointConfigurator.class)
@Component
public class UserServerEndpoint {

    private static final CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
    private static long count;
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock LOCK_READ = LOCK.readLock();
    private static final Lock LOCK_WRITE = LOCK.writeLock();
    private static final ThreadLocal<Session> THREAD_LOCAL = new ThreadLocal<>();

    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        setCount(1);
        System.out.println("--> ServerEndpoint onOpen() --> " + session);
        System.out.println("--> ServerEndpoint onOpen() --> count: " + getCount());
        THREAD_LOCAL.set(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("--> ServerEndpoint onMessage() --> " + session);
        System.out.println("--> ServerEndpoint onMessage() --> message: " + message);
        for (Session s : sessionSet) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose() {
        Session session = THREAD_LOCAL.get();
        sessionSet.remove(session);
        THREAD_LOCAL.remove();
        setCount(-1);
        System.out.println("--> ServerEndpoint onClose() --> " + getCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("--> ServerEndpoint onError() --> " + error.getMessage());
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
