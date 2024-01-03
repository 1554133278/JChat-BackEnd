package com.ygw.common.entty;

import lombok.Data;

import java.io.Serializable;

@Data
public class NettyMsg implements Serializable{

    /**
     *  1.新连接
     *  2.心跳
     *  3.单聊
     *  4.正在输入
     *  5.结束输入
     *  6.挤下线
     */
    private Integer type;

    /**
     * 客户端的设备id
     */
    private String did;
}
