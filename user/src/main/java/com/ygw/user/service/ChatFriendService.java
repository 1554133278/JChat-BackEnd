package com.ygw.user.service;

import com.ygw.user.entity.ChatFriend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【chat_friend】的数据库操作Service
* @createDate 2023-12-18 23:40:35
*/
public interface ChatFriendService extends IService<ChatFriend> {

    List<ChatFriend> getFriendListByUserId(Long userId, List<Long> targetIdList);

    boolean insertOrUpdateFriend(ChatFriend friend);

}
