package com.ygw.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygw.common.constant.CommonConstant;
import com.ygw.user.entity.ChatUser;
import com.ygw.user.service.ChatUserService;
import com.ygw.user.mapper.ChatUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
* @author Administrator
* @description 针对表【chat_user】的数据库操作Service实现
* @createDate 2023-12-18 23:41:23
*/
@Service
public class ChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUser> implements ChatUserService{
    @Resource
    private ChatUserMapper userMapper;

    @Override
    public ChatUser getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<ChatUser> getUserListByIds(List<Long> ids) {
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        return userMapper.selectList(wrapper);
    }

    @Override
    public ChatUser getUserByChatNo(String chatNo) {
        if (Objects.isNull(chatNo)){
            return null;
        }
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq("chat_no", chatNo);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public boolean insertOrUpdateUser(ChatUser user) {
        if (Objects.isNull(user)){
            return false;
        }
        // 需要判断有没有重复对象
        if (Objects.isNull(user.getId())){
            //  插入
            user.setGmtCreate(new Date());
            return userMapper.insert(user) > CommonConstant.NumConstant.INT_ZERO;
        }
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id", user.getId());
        ChatUser existUser = userMapper.selectOne(wrapper);
        if (Objects.isNull(existUser)){
            //  插入
            user.setGmtCreate(new Date());
            return userMapper.insert(user) > CommonConstant.NumConstant.INT_ZERO;
        }
        //  更新
        user.setId(existUser.getId());
        user.setGmtModified(new Date());
        user.setGmtCreate(existUser.getGmtCreate());
        return userMapper.updateById(user) > CommonConstant.NumConstant.INT_ZERO;
    }

    @Override
    public boolean isExist(ChatUser user) {
        if (Objects.isNull(user)){
            return false;
        }
        // 判断有没有重复对象
        QueryWrapper<ChatUser> wrapper = new QueryWrapper<>();
        wrapper.eq("chat_no", user.getChatNo()).or().eq("phone", user.getChatNo());
        return userMapper.exists(wrapper);
    }
}




