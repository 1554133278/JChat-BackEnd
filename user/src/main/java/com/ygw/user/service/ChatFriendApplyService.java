package com.ygw.user.service;

import com.ygw.user.entity.ChatFriendApply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【chat_friend_apply】的数据库操作Service
* @createDate 2023-12-18 23:41:14
*/
public interface ChatFriendApplyService extends IService<ChatFriendApply> {

    List<ChatFriendApply> getApplyListByTargetId(Long userId);

    ChatFriendApply getApplyById(Long id);

    boolean insertOrUpdateFriendApply(ChatFriendApply apply);
}
