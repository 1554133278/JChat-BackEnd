package com.ygw.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygw.common.constant.CommonConstant;
import com.ygw.user.entity.ChatFriendApply;
import com.ygw.user.enums.ApplyStatusEnum;
import com.ygw.user.service.ChatFriendApplyService;
import com.ygw.user.mapper.ChatFriendApplyMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Administrator
* @description 针对表【chat_friend_apply】的数据库操作Service实现
* @createDate 2023-12-18 23:41:14
*/
@Service
public class ChatFriendApplyServiceImpl extends ServiceImpl<ChatFriendApplyMapper, ChatFriendApply> implements ChatFriendApplyService {
    @Resource
    private ChatFriendApplyMapper applyMapper;

    @Override
    public List<ChatFriendApply> getApplyListByTargetId(Long userId) {
        if (Objects.isNull(userId)){
            return Collections.emptyList();
        }
        QueryWrapper<ChatFriendApply> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id", userId);
        return applyMapper.selectList(wrapper);
    }

    @Override
    public ChatFriendApply getApplyById(Long id) {
        return applyMapper.selectById(id);
    }

    @Override
    public boolean insertOrUpdateFriendApply(ChatFriendApply apply) {
        if (Objects.isNull(apply)){
            return false;
        }
        // 需要判断有没有重复对象
        QueryWrapper<ChatFriendApply> wrapper = new QueryWrapper<>();
        wrapper.eq("id", apply.getId());
        wrapper.or().eq("user_id", apply.getUserId()).eq("target_id", apply.getTargetId());
        ChatFriendApply existApply = applyMapper.selectOne(wrapper);
        if (Objects.isNull(apply.getStatus())){
            apply.setStatus(ApplyStatusEnum.INIT.getCode());
        }
        if (Objects.isNull(existApply)){
            //  插入
            apply.setGmtCreate(new Date());
            return applyMapper.insert(apply) > CommonConstant.NumConstant.INT_ZERO;
        }
        //  更新
        apply.setId(existApply.getId());
        apply.setGmtModified(new Date());
        apply.setGmtCreate(existApply.getGmtCreate());
        return applyMapper.updateById(apply) > CommonConstant.NumConstant.INT_ZERO;
    }
}




