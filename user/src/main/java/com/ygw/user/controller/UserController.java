package com.ygw.user.controller;

import com.ygw.common.constant.CommonConstant;
import com.ygw.common.entty.ShutDownMsg;
import com.ygw.user.entity.ChatUser;
import com.ygw.common.result.Response;
import com.ygw.user.service.ChatFriendApplyService;
import com.ygw.user.service.ChatFriendService;
import com.ygw.user.service.ChatUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygw
 * @createDate 2023/12/19 21:51
 **/
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Resource
    private ChatUserService userService;
    @Resource
    private ChatFriendService friendService;
    @Resource
    private ChatFriendApplyService applyService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/getById")
    public Response<ChatUser> getUserById(Long id){
        return Response.success(userService.getUserById(id));
    }

    @GetMapping("/getUserListByIds")
    public Response<List<ChatUser>> getUserListByIds(String ids){
        if (StringUtils.isNotBlank(ids)){
            List<String> list = Arrays.stream(ids.split(",")).toList();
            List<Long> longList = list.stream().map(Long::parseLong).collect(Collectors.toList());
            return Response.success(userService.getUserListByIds(longList));
        } else {
            return Response.success(Collections.emptyList());
        }
    }

    @GetMapping("/getByChatNo")
    public Response<ChatUser> getUserByChatNo(String chatNo){
        return Response.success(userService.getUserByChatNo(chatNo));
    }

    @GetMapping("/register")
    public Response<String> register(ChatUser user){
        boolean exist = userService.isExist(user);
        if (exist){
            return Response.error("Couldn't repeat register for the same chatNo or phone,please check your input!");
        }
        boolean result = userService.insertOrUpdateUser(user);
        return Response.success(result ? "Register Success" : "Register Error,please try again!");
    }

    @GetMapping("/update")
    public Response<String> update(ChatUser user){
        boolean result = userService.insertOrUpdateUser(user);
        return Response.success(result ? "Update Success" : "Update Error,please try again!");
    }

    @GetMapping("/hello")
    public Response<String> getUserListById(){
        return Response.success("Hello world!I'm userService.");
    }

    @GetMapping( "/login")
    public Response<ChatUser> login(String chatNo, String password,String did) {
        ChatUser user = userService.getUserByChatNo(chatNo);
        if(user != null && user.getPassword().equals(password)){
            user.setPassword(null); // 密码不能写到手机里面

            // 1.先根据用户id查询设备id
            String redisDid = stringRedisTemplate.opsForValue().get(user.getId().toString());
            if(redisDid != null && !did.equals(redisDid)){
                log.info("发送给交换机");
                // 挤下其他的设备
                ShutDownMsg shutDownMsg = new ShutDownMsg();
                shutDownMsg.setDid(redisDid); // redis中的设备id
                rabbitTemplate.convertAndSend("chat_exchange","",shutDownMsg);
            }
            // 登录成功后要保护用户id和设备id
            stringRedisTemplate.opsForValue().set(user.getId().toString(),did);

            return Response.success(user);
        }else{
            return Response.error("ChatNo or Password error,please check!");
        }
    }
}
