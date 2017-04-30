package com.forsrc.boot.websocket.user;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.forsrc.boot.config.WebsocketConfig;

@ServerEndpoint(value = "/wss/user", configurator = WebsocketConfig.ServerEndpointConfigurator.class)
@Component
public class UserServerEndpoint {

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
        System.out.println(String.format("--> ServerEndpoint onOpen() --> %s", session));
        System.out.println(String.format("--> ServerEndpoint onOpen() --> count: %s", getCount()));
        THREAD_LOCAL.set(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(String.format("--> ServerEndpoint onMessage() --> %s", session));
        System.out.println(String.format("--> ServerEndpoint onMessage() --> message: %s", message));
        for (Session s : sessionSet) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (message != null && message.equals(String.format("CLOSE: %s", session.getId()))) {
            close(session);
        }
    }

    @OnClose
    public void onClose() {
        System.out.println(String.format("--> ServerEndpoint onClose() --> %s", getCount()));
        Session session = THREAD_LOCAL.get();
        close(session);
        sessionSet.remove(session);
        THREAD_LOCAL.remove();
        setCount(-1);
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
        System.out.println(String.format("--> ServerEndpoint onError() --> %s", error.getMessage()));
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
