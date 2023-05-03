package com.sondy.run_java.app.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Mapper
public interface UserMapper {

    public List<HashMap<String, Object>> getUserList();
}
