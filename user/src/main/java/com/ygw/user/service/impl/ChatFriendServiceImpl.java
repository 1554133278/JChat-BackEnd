package com.ygw.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygw.common.constant.CommonConstant;
import com.ygw.user.entity.ChatFriend;
import com.ygw.user.service.ChatFriendService;
import com.ygw.user.mapper.ChatFriendMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Administrator
* @description 针对表【chat_friend】的数据库操作Service实现
* @createDate 2023-12-18 23:40:35
*/
@Service
@Slf4j
public class ChatFriendServiceImpl extends ServiceImpl<ChatFriendMapper, ChatFriend> implements ChatFriendService {
    @Resource
    private ChatFriendMapper friendMapper;

    @Override
    public List<ChatFriend> getFriendListByUserId(Long userId, List<Long> targetIdList) {
        if (Objects.isNull(userId)){
            return Collections.emptyList();
        }
        QueryWrapper<ChatFriend> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (CollectionUtils.isNotEmpty(targetIdList)){
            wrapper.in("target_id", targetIdList);
        }
        return friendMapper.selectList(wrapper);
    }

    @Override
    public boolean insertOrUpdateFriend(ChatFriend friend) {
        if (Objects.isNull(friend)){
            return false;
        }
        // 需要判断有没有重复对象
        QueryWrapper<ChatFriend> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", friend.getUserId());
        wrapper.eq("target_id", friend.getTargetId());
        ChatFriend existFriend = friendMapper.selectOne(wrapper);
        if (Objects.isNull(existFriend)){
            //  插入
            friend.setGmtCreate(new Date());
            return friendMapper.insert(friend) > CommonConstant.NumConstant.INT_ZERO;
        }
        //  更新
        friend.setId(existFriend.getId());
        friend.setGmtModified(new Date());
        friend.setGmtCreate(existFriend.getGmtCreate());
        return friendMapper.updateById(friend) > CommonConstant.NumConstant.INT_ZERO;
    }
}




