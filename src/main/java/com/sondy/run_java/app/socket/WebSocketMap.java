package com.sondy.run_java.app.socket;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@Service
@ServerEndpoint(value="/socket/{nickName}")
public class WebSocketMap {
    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());

    private static HashMap<Session,Object> currentUser = new HashMap<>();


    @OnOpen
    public void onOpen(Session s, @PathParam("nickName") String nickName) {
        System.out.println("open session : " + s.toString());
        System.out.println("nickName : " + nickName);
        if(!clients.contains(s)) {
            clients.add(s);
            System.out.println("session open : " + s);
        }else {
            System.out.println("이미 연결된 session 임!!!");
        }

        if(!currentUser.containsKey(s)){
            currentUser.put(s,new HashMap<String,Object>());
        }else {
            System.out.println("이미 달리는 유저");
        }

    }


    @OnMessage
    public void onMessage(String msg, Session session) throws Exception{
        System.out.println("receive message : " + msg);
        Gson gson = new Gson();
        HashMap<String,Object> tempMap = new HashMap<>();

        //1. 메세지를 json에서 hashMap으로
        tempMap = gson.fromJson(msg,tempMap.getClass());

        //2. 세션맵에 담겨있던 데이터를 파라미터로 받은 세션을 키로 이용해서 데이터를 가져온다.
        HashMap<String,Object> storedMap = (HashMap<String, Object>) currentUser.get(session);

        HashMap<String,Object> cood

        //3. 좌표를 리스트화 시켜서 map에 저장한다.
        List<HashMap<String,Object>> historyList = (List<HashMap<String, Object>>) storedMap.get("list");
        if(historyList!=null){
            historyList = new ArrayList<>();
            historyList.
        }

        for(Session s : clients) {
            System.out.println("send data : " + msg);
            s.getBasicRemote().sendText(msg);

        }

    }

    @OnClose
    public void onClose(Session s) {
        System.out.println("session close : " + s);
        clients.remove(s);
        currentUser.remove(s);
    }
}
