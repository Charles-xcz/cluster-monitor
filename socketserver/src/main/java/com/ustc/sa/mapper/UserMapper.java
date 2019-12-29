package com.ustc.sa.mapper;

import com.ustc.sa.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    boolean add(User user);


    User selectByUsername(String username);

//    @Select("select *from user")
    List<User> queryAll();

}
