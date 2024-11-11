package com.intern.ChatApp.config.websocket;


import com.intern.ChatApp.utils.JwtUtilsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = request.getURI().getQuery();
        System.out.println(token);
        if (token == null || !token.startsWith("token=")) {
            response.setStatusCode(HttpStatus.FORBIDDEN);

            return false;
        }

        token = token.split("=")[1];

        if (!jwtUtilsHelper.validateToken(token)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);

            return false;
        }

        String email = jwtUtilsHelper.extractUsername(token);
        attributes.put("email", email);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
