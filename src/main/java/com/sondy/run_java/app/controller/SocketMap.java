package com.sondy.run_java.app.controller;

import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
//
//@Service
////@ServerEndpoint(value = "/socket")
//@ServerEndpoint(value="/socket")
//public class SocketMap {
//        private static Set<Session> clients =
//                Collections.synchronizedSet(new HashSet<Session>());
//    @OnOpen
//    public void onOpen(Session session) throws IOException {
//        System.out.println("TESTS@@@@@@");
//        // Get session and WebSocket connection
//    }
//
//        @OnMessage
//    public void onMessage(String msg, Session session) throws Exception {
//            System.out.println("receive message : " + msg);
//            for (Session s : clients) {
//                System.out.println("send data : " + msg);
//                s.getBasicRemote().sendText(msg);
//
//            }
//        }
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        System.out.println("TESTS@@@@@@");
//        // WebSocket connection closes
//    }

//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        System.out.println("TESTS@@@@@@");
//        // Do error handling here
//    }
//}
