package com.sondy.run_java.app.socket;

import com.google.gson.Gson;
import com.sondy.run_java.app.mapper.UserMapper;
import com.sondy.run_java.app.service.UserService;
import com.sondy.run_java.global.ServerEndpointConfigurator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
@RequiredArgsConstructor
@Service
@ServerEndpoint(value="/socket/{nickName}/{userShow}",configurator = ServerEndpointConfigurator.class)
public class WebSocketMap {
    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());

    private static HashMap<Session,Object> currentUser = new HashMap<>();

    private final UserService userService;

    @OnOpen
    public void onOpen(Session s, @PathParam("nickName") String nickName, @PathParam("userShow") String userShow) {
        System.out.println("open session : " + s.toString());
        System.out.println("nickName : " + nickName);
        if(!clients.contains(s)) {
            clients.add(s);
            System.out.println("session open : " + s);
        }else {
            System.out.println("이미 연결된 session 임!!!");
        }

        if(!currentUser.containsKey(s)){
            HashMap<String,Object> user = new HashMap<>();
            user.put("nickName",nickName);
            user.put("userShow",userShow);
            int runPk = userService.makeRun(user);
            currentUser.put(s,user);

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

        HashMap<String,Object> coords = new HashMap<>();
        coords.put("longitude",tempMap.get("longitude"));
        coords.put("latitude",tempMap.get("latitude"));
        coords.put("reg_dt",tempMap.get("reg_dt"));

        //3. 좌표를 리스트화 시켜서 map에 저장한다.
        List<HashMap<String,Object>> historyList = (List<HashMap<String, Object>>) storedMap.get("list");

        if(historyList==null){
            historyList = new ArrayList<>();
        }

        historyList.add(coords);
        storedMap.put("list",historyList);

        if("Y".equals(storedMap.get("userShow"))){
            for(Session s : clients) {
                HashMap<String,Object> otherUser = (HashMap<String, Object>) currentUser.get(s);
                if("Y".equals(otherUser.get("userShow"))&&s!=session){
                    System.out.println("send data : " + msg);
                    s.getBasicRemote().sendText(msg);
                }
            }
        }

        tempMap.put("run_pk",storedMap.get("run_pk"));
        tempMap.put("user_pk",storedMap.get("user_pk"));
        //DB화
        userService.insertMapHistory(tempMap);


    }

    @OnClose
    public void onClose(Session s) {
        System.out.println("session close : " + s);

        HashMap<String,Object> userMap = (HashMap<String, Object>) currentUser.get(s);

        System.out.println(userMap.toString());
        System.out.println(userMap.toString());
        System.out.println(userMap.toString());
        System.out.println(userMap.toString());


        clients.remove(s);
        currentUser.remove(s);
    }
}
