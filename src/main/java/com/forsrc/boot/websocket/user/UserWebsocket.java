package com.forsrc.boot.websocket.user;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

@ServerEndpoint(value = "/websocket/ws/user", configurator = SpringConfigurator.class)
@Component
public class UserWebsocket {

    private static final CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
    private static long count;
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock LOCK_READ = LOCK.readLock();
    private static final Lock LOCK_WRITE = LOCK.writeLock();
    private static final ThreadLocal<Session> THREAD_LOCAL = new ThreadLocal<>();

    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        System.out.println("--> onOpen() --> " + session);
        System.out.println("--> onOpen() --> count: " + getCount());
        setCount(1);
        THREAD_LOCAL.set(session);
        try {
            session.getAsyncRemote().sendText(session.getId() + " --> hello world, " + new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("--> onMessage() --> " + session);
        System.out.println("--> onMessage() --> message: " + message);
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
        System.out.println("--> onClose() --> " + getCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("--> onError() --> " + error.getMessage());
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
