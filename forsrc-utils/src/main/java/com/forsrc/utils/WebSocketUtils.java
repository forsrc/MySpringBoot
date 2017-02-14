package com.forsrc.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;


/**
 * The type Web socket utils.
 */
public class WebSocketUtils {

    //public static final String SessionAttribute = "isWEB";

    /**
     * Forbidden response string.
     *
     * @return the string
     */
// Construct a successful websocket handshake response using the key param
    // (See RFC 6455).
    public static String forbiddenResponse() {

        // String response = "HTTP/1.1 101 Web Socket Protocol Handshake\r\n";
        StringBuilder response = new StringBuilder("HTTP/1.1 403 Forbidden\r\n")
                .append("Connection: close\r\n")
                .append("Content-Length: 0\r\n")
                .append("\r\n");
        return response.toString();
    }

    /**
     * Handshake response string.
     *
     * @param key the key
     * @return the string
     */
    public static String handshakeResponse(final String key) {

        // String response = "HTTP/1.1 101 Web Socket Protocol Handshake\r\n";
        StringBuilder response = new StringBuilder("HTTP/1.1 101 Switching Protocols\r\n")

                .append("Upgrade: websocket\r\n")
                .append("Connection: Upgrade\r\n")
                .append("Sec-WebSocket-Accept: ")
                .append(key)
                .append("\r\n")
                // added by cooper 2015-06-30
                .append("Sec-WebSocket-Protocol: v10.stomp\r\n")
                .append("\r\n");
        return response.toString();
    }


    /**
     * Parse request map.
     *
     * @param WSRequest the ws request
     * @return the map
     */
    public static Map<String, String> parseRequest(final String WSRequest) {
        HashMap<String, String> ret = new HashMap<String, String>();
        String[] headers = WSRequest.split("\r\n");
        String socketKey = null;
        for (int i = 1; i < headers.length; i++) {
            String line = headers[i];
            int delimiter = line.indexOf(":");
            if (delimiter <= 0) {
                break;
            }
            String name = line.substring(0, delimiter);
            String value = line.substring(delimiter + 1).trim();
            ret.put(name, value);
        }
        return ret;
    }

    /**
     * Gets client ws request key.
     *
     * @param WSRequest the ws request
     * @return the client ws request key
     */
// Parse the string as a websocket request and return the value from
    // Sec-WebSocket-Key header (See RFC 6455). Return empty string if not found.
    public static String getClientWSRequestKey(final String WSRequest) {


        String[] headers = WSRequest.split("\r\n");
        String socketKey = null;
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].contains("Sec-WebSocket-Key")) {
                String[] info = headers[i].split(":");
                socketKey = info.length >= 2 ? info[1].trim() : "";
                break;
            }
        }
        return socketKey;
    }

    /**
     * Gets web socket key challenge response.
     *
     * @param challenge the challenge
     * @return the web socket key challenge response
     * @throws NoSuchAlgorithmException     the no such algorithm exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
//
    // Builds the challenge response to be used in WebSocket handshake.
    // First append the challenge with "258EAFA5-E914-47DA-95CA-C5AB0DC85B11" and then
    // make a SHA1 hash and finally Base64 encode it. (See RFC 6455)
    public static String getWebSocketKeyChallengeResponse(final String challenge) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String challengeStr = challenge;
        challengeStr.concat("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
        MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();
        cript.update(challenge.getBytes("utf8"));
        byte[] hashedVal = cript.digest();
        return new String(new Base64().encode(hashedVal));
    }
}