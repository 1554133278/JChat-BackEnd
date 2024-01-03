package com.ygw.controller;

import com.ygw.common.result.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ygw
 * @createDate 2023/12/23 11:43
 **/
@RequestMapping("/netty")
@RestController
public class TestController {
    @GetMapping("/hello")
    public Response<String> getUserById(){
        return Response.success("Hello world!I'm nettyService");
    }
}
