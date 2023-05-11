package com.sondy.run_java.app.socket;

import com.google.gson.Gson;
import com.sondy.run_java.app.mapper.UserMapper;
import com.sondy.run_java.app.service.UserService;
import com.sondy.run_java.global.ServerEndpointConfigurator;
import com.sondy.run_java.global.utils.getDistance;
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
import java.io.IOException;
import java.util.*;
@RequiredArgsConstructor
@Service
@ServerEndpoint(value="/socket/{nickName}/{userShow}/{addr}",configurator = ServerEndpointConfigurator.class)
public class WebSocketMap {
    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());

    private static HashMap<Session,Object> currentUser = new HashMap<>();

    private final UserService userService;

    private HashMap<String,Object> userLocaData = new HashMap<>();

    @OnOpen
    public void onOpen(
            Session s,
            @PathParam("nickName") String nickName,
            @PathParam("userShow") String userShow,
            @PathParam("addr") String addr

        ) {
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
            user.put("addr",addr);
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

                    HashMap<String,Object> aTempMap = (HashMap<String, Object>) userLocaData.get(otherUser.get("nickName"));

                    if(aTempMap!=null){
                        Double beforeLat = Double.parseDouble((String) aTempMap.get("latitude"));
                        Double beforeLong = Double.parseDouble((String) aTempMap.get("longitude"));
                        Double curLat = Double.parseDouble((String) tempMap.get("latitude"));
                        Double curLong = Double.parseDouble((String) tempMap.get("longitude"));
                        Double distance = getDistance.distance(beforeLat,beforeLong,curLat,curLong,"kilometer");
                        if(distance.intValue()<=10){
                            System.out.println("send data : " + msg);
                            s.getBasicRemote().sendText(msg);
                        }
                    }
                }
            }
        }

        //마지막 좌표와 거리계산
        HashMap<String,Object> myMap = (HashMap<String, Object>) userLocaData.get(tempMap.get("nickName"));
        if(myMap!=null){
            Double beforeLat = Double.parseDouble((String) myMap.get("latitude"));
            Double beforeLong = Double.parseDouble((String) myMap.get("longitude"));
            Double curLat = Double.parseDouble((String) tempMap.get("latitude"));
            Double curLong = Double.parseDouble((String) tempMap.get("longitude"));
            Double distance = getDistance.distance(beforeLat,beforeLong,curLat,curLong,"meter");
            tempMap.put("distance",distance.intValue());
            userLocaData.put((String) tempMap.get("nickName"),tempMap);
            session.getBasicRemote().sendText(gson.toJson(tempMap));
        }else {
            userLocaData.put((String) tempMap.get("nickName"),tempMap);
            tempMap.put("distance",0);
        }
        tempMap.put("run_pk",storedMap.get("run_pk"));
        tempMap.put("user_pk",storedMap.get("user_pk"));
        //DB화
        userService.insertMapHistory(tempMap);

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("session close : " + session);
        HashMap<String,Object> curUser = (HashMap<String, Object>) currentUser.get(session);
        for(Session s : clients) {
            HashMap<String,Object> otherUser = (HashMap<String, Object>) currentUser.get(s);
            if("Y".equals(otherUser.get("userShow"))&&s!=session){

                HashMap<String,Object> aTempMap = (HashMap<String, Object>) userLocaData.get(otherUser.get("nickName"));
                HashMap<String,Object> bTempMap = (HashMap<String, Object>) userLocaData.get(curUser.get("nickName"));

                if(aTempMap!=null){
                    Double beforeLat = Double.parseDouble((String) aTempMap.get("latitude"));
                    Double beforeLong = Double.parseDouble((String) aTempMap.get("longitude"));
                    Double curLat = Double.parseDouble((String) bTempMap.get("latitude"));
                    Double curLong = Double.parseDouble((String) bTempMap.get("longitude"));
                    Double distance = getDistance.distance(beforeLat,beforeLong,curLat,curLong,"kilometer");
                    if(distance.intValue()<=10){
                        HashMap<String,Object> msgMap = new HashMap<>();
                        Gson gson = new Gson();
                        msgMap.put("nickName",curUser.get("nickName"));
                        msgMap.put("finishedUser","Y");
                        s.getBasicRemote().sendText(gson.toJson(msgMap));
                    }
                }
            }
        }

        clients.remove(session);
        currentUser.remove(session);
    }
}
