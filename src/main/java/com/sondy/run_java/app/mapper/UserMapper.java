package com.sondy.run_java.app.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Mapper
public interface UserMapper {

    public List<HashMap<String, Object>> getUserList();

    public int getUserCd(String nickName);
    public int insertRun(HashMap<String,Object> paramMap);
    public int insertHistory(HashMap<String,Object> paramMap);
    public int insertUser(HashMap<String,Object> paramMap);
    public List<HashMap<String,Object>> getUserRunHistory (HashMap<String,Object> paramMap);
    public List<HashMap<String,Object>> getUserRunHistoryDetail (HashMap<String,Object> paramMap);

}
