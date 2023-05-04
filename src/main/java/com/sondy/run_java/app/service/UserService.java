package com.sondy.run_java.app.service;

import com.sondy.run_java.app.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public HashMap<String, Object> getUserList(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("list",userMapper.getUserList());

        return result;
    }

    public int makeRun(HashMap<String,Object> paramMap){
        int userPk = userMapper.getUserCd((String)paramMap.get("nickName"));
        paramMap.put("user_pk",userPk);

        return userMapper.insertRun(paramMap);
    }

    public HashMap<String,Object> insertMapHistory(HashMap<String,Object> paramMap){
        HashMap<String,Object> resultMap = new HashMap<>();

        userMapper.insertHistory(paramMap);

        return resultMap;
    }

    public int insertUser(HashMap<String,Object> paramMap){
        return userMapper.insertUser(paramMap);
    }
}
