package com.forsrc.boot.websocket;

import com.forsrc.boot.websocket.user.UserClientEndpoint;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WebsocketTest {

    @Test
    public void test() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = URI.create("wss://localhost:8075/websocket/ws/user/");
        Session session = container.connectToServer(UserClientEndpoint.class, uri);
        session.getBasicRemote().sendText("77");
        session.getBasicRemote().sendText("75");
    }
}
