package com.ygw.common.result;

import com.ygw.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ygw
 * @createDate 2023/12/19 21:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private static final String SUCCESS = "success";

    private Integer code;
    private Boolean success;
    private T data;
    private String msg;
    private PageData meta;

    public static <T> Response<T> success(T data){
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMsg(Response.SUCCESS);
        response.setCode(CommonConstant.NetWorkConstant.SUCCESS);
        return response;
    }

    public static <T> Response<T> success(T data, PageData page){
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMeta(page);
        response.setMsg(Response.SUCCESS);
        response.setCode(CommonConstant.NetWorkConstant.SUCCESS);
        return response;
    }

    public static <T> Response<T> error(String errMsg){
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMsg(errMsg);
        response.setCode(CommonConstant.NetWorkConstant.ERROR);
        return response;
    }


}
