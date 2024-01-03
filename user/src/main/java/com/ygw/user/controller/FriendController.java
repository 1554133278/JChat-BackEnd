package com.ygw.user.controller;

import com.alibaba.fastjson.JSON;
import com.ygw.common.constant.CommonConstant;
import com.ygw.user.entity.ChatFriend;
import com.ygw.user.entity.ChatFriendApply;
import com.ygw.user.entity.ChatUser;
import com.ygw.user.enums.ApplyStatusEnum;
import com.ygw.user.enums.FriendStatusEnum;
import com.ygw.common.result.Response;
import com.ygw.user.service.ChatFriendApplyService;
import com.ygw.user.service.ChatFriendService;
import com.ygw.user.service.ChatUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ygw
 * @createDate 2023/12/19 22:39
 **/
@RequestMapping("/friend")
@RestController
@Slf4j
public class FriendController {
    @Resource
    private ChatFriendService friendService;
    @Resource
    private ChatFriendApplyService applyService;
    @Resource
    private ChatUserService userService;

    @GetMapping("/getFriendListByUserId")
    public Response<List<ChatFriend>> getFriendListByUserId(Long userId,@RequestBody(required = false) String targetIds){
        List<Long> targetIdList = null;
        if (StringUtils.isNotBlank(targetIds)){
            List<String> list = Arrays.stream(targetIds.split(",")).toList();
            targetIdList = list.stream().map(Long::parseLong).collect(Collectors.toList());
        }
        List<ChatFriend> friendList = friendService.getFriendListByUserId(userId, targetIdList);
        if (CollectionUtils.isNotEmpty(friendList)){
            List<ChatUser> userList = userService.getUserListByIds(friendList.stream().map(ChatFriend::getTargetId).distinct().collect(Collectors.toList()));
            Map<Long, List<ChatUser>> map = userList.stream().collect(Collectors.groupingBy(ChatUser::getId));
            for (ChatFriend friend : friendList) {
                List<ChatUser> list = map.get(friend.getTargetId());
                if (CollectionUtils.isNotEmpty(list)){
                    friend.setFriend(list.get(CommonConstant.NumConstant.INT_ZERO));
                }
            }
        }
        return Response.success(friendList);
    }

    @GetMapping("/hello")
    public Response<String> hello(){
        return Response.success("Hello world!I'm friendService.");
    }

    @GetMapping("/updateFriend")
    public Response<Boolean> updateFriend(ChatFriend friend){
        return Response.success(friendService.insertOrUpdateFriend(friend));
    }

    @GetMapping("/getApplyListByUserId")
    public Response<List<ChatFriendApply>> getApplyListByUserId(Long userId){
        List<ChatFriendApply> applyList = applyService.getApplyListByTargetId(userId);
        if (CollectionUtils.isNotEmpty(applyList)){
            List<ChatUser> userList = userService.getUserListByIds(applyList.stream().map(ChatFriendApply::getUserId).distinct().collect(Collectors.toList()));
            Map<Long, List<ChatUser>> map = userList.stream().collect(Collectors.groupingBy(ChatUser::getId));
            for (ChatFriendApply apply : applyList) {
                List<ChatUser> list = map.get(apply.getUserId());
                if (CollectionUtils.isNotEmpty(list)){
                    apply.setFriend(list.get(CommonConstant.NumConstant.INT_ZERO));
                }
            }
        }
        return Response.success(applyList);
    }

    @GetMapping("/updateApply")
    public Response<Boolean> updateApply(ChatFriendApply apply){
        //  判断申请情况
        //  不存在原本申请增加
        if (Objects.isNull(apply.getId())){
            return Response.success(applyService.insertOrUpdateFriendApply(apply));
        }
        //  存在原本申请看是否通过
        ChatFriendApply updateApply = applyService.getApplyById(apply.getId());
        updateChatApply(apply, updateApply);
        boolean result = applyService.insertOrUpdateFriendApply(updateApply);
        if (result && ApplyStatusEnum.APPROVE.getCode().equals(apply.getStatus())){
            //  构建朋友关系并插入数据库
            ChatFriend friend = new ChatFriend();
            friend.setStatus(FriendStatusEnum.NORMAL.getCode());
            friend.setTargetId(updateApply.getTargetId());
            friend.setUserId(updateApply.getUserId());
            friendService.insertOrUpdateFriend(friend);

            ChatFriend toFriend = new ChatFriend();
            toFriend.setStatus(FriendStatusEnum.NORMAL.getCode());
            toFriend.setTargetId(updateApply.getUserId());
            toFriend.setUserId(updateApply.getTargetId());
            friendService.insertOrUpdateFriend(toFriend);
        }
        return Response.success(result);
    }

    private void updateChatApply(ChatFriendApply apply, ChatFriendApply updateApply) {
        if (!Objects.isNull(apply.getStatus())){
            updateApply.setStatus(apply.getStatus());
        }
        if (!Objects.isNull(apply.getUserId())){
            updateApply.setUserId(apply.getUserId());
        }
        if (!Objects.isNull(apply.getTargetId())){
            updateApply.setTargetId(apply.getTargetId());
        }
        if (StringUtils.isNotBlank(apply.getMsg())){
            updateApply.setMsg(apply.getMsg());
        }
    }
}
