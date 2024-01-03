package com.ygw.user.service;

import com.ygw.user.entity.ChatUser;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
* @author Administrator
* @description 针对表【chat_user】的数据库操作Service
* @createDate 2023-12-18 23:41:23
*/
public interface ChatUserService extends IService<ChatUser> {

    ChatUser getUserById(Long id);

    List<ChatUser> getUserListByIds(List<Long> ids);

    ChatUser getUserByChatNo(String chatNo);

    boolean insertOrUpdateUser(ChatUser user);

    boolean isExist(ChatUser user);
}
