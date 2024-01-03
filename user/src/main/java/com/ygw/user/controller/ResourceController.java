package com.ygw.user.controller;

import com.alibaba.fastjson.JSON;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.ygw.common.result.Response;
import com.ygw.user.entity.ChatUser;
import com.ygw.user.service.ChatUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/res")
@Slf4j
public class ResourceController {
    // tacker
    // storage
    @Resource
    private FastFileStorageClient fileStorageClient;
    @Resource
    private ChatUserService userService;

    @Value("${fastpath}")
    private String fdfsPath;

    @RequestMapping(value = "/uploadHeadImg")
    public Response<Map<String,String>> uploadHeadImg(Long userId, MultipartFile file){
        String fileName = file.getOriginalFilename();

        // 获取文件的后缀
        String fileExName = fileName.substring(fileName.lastIndexOf(".")+1);
        try {

            // 上传头像到FastDFS
            StorePath storePath = fileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), fileExName, null);
            String fullPath = storePath.getFullPath();

            // 获取大图和小图的路径
            String maxHead = fdfsPath+fullPath;
            String minHead = fdfsPath+fullPath.replaceAll("."+fileExName,"_100x100."+fileExName);

            // 修改用户表中的头像
            if(userId != null){
                ChatUser user = userService.getUserById(userId);
                user.setMaxHead(maxHead);
                user.setMinHead(minHead);
                userService.insertOrUpdateUser(user);
            }
            Map<String,String> map = new HashMap<>();
            map.put("maxHead",maxHead);
            map.put("minHead",minHead);
            log.info("uploadHeadImg,result:{}", map);
            return Response.success(map);
        } catch (IOException e) {
           log.error("uploadHeadImg,error,userId:{},file:{},msg:{}", userId, JSON.toJSONString(file), e.getMessage() ,e);
           return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/uploadImg")
    public Response<String> uploadImg(Long userId, MultipartFile file){
        String fileName = file.getOriginalFilename();

        // 获取文件的后缀
        String fileExName = fileName.substring(fileName.lastIndexOf(".")+1);
        try {
            // 上传图片到FastDFS
            StorePath storePath = fileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), fileExName, null);
            String fullPath = storePath.getFullPath();

            log.info("uploadImg,result:{}", fdfsPath+fullPath);
            return Response.success(fdfsPath+fullPath);
        } catch (IOException e) {
            log.error("uploadImg,error，userId:{},file:{},msg:{}", userId, JSON.toJSONString(file), e.getMessage() ,e);
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/uploadFile")
    public Response<String> uploadFile(Long userId, MultipartFile file){
        String fileName = file.getOriginalFilename();

        // 获取文件的后缀
        String fileExName = fileName.substring(fileName.lastIndexOf(".")+1);
        try {
            // 上传图片到FastDFS
            StorePath storePath = fileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExName, null);
            String fullPath = storePath.getFullPath();

            log.info("uploadFile,result:{}", fdfsPath+fullPath);
            return Response.success(fdfsPath+fullPath);
        } catch (IOException e) {
            log.error("uploadFile,error，userId:{},file:{},msg:{}", userId, JSON.toJSONString(file), e.getMessage() ,e);
            return Response.error(e.getMessage());
        }
    }
}
