package com.forsrc.boot.websocket;

import com.forsrc.boot.websocket.user.UserClientEndpoint;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.concurrent.TimeUnit;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WebsocketTest {

    @Test
    public void test() throws Exception {

        System.setProperty("javax.net.debug", "all");

        System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("truststore.keystore").getFile().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("client.jks").getFile().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", "apache");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = URI.create("wss://localhost:8075/wss/user");
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();


        Session session = container.connectToServer(UserClientEndpoint.class, uri);
        //session.getBasicRemote().sendText("77");
        //session.getBasicRemote().sendText("75");
        session.setMaxIdleTimeout(60 * 1000);
        TimeUnit.SECONDS.sleep(60);

        session.close();
    }
}
