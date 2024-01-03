package com.ygw.user.mapper;

import com.ygw.user.entity.ChatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【chat_user】的数据库操作Mapper
* @createDate 2023-12-18 23:41:23
* @Entity com.ygw.entity.ChatUser
*/
@Mapper
public interface ChatUserMapper extends BaseMapper<ChatUser> {

}




