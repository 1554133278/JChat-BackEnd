package com.ygw.common.entty;

import lombok.Data;

@Data
public class ChatMsg extends  NettyMsg {
    private Integer fid;
    private Integer tid;
    // 内容
    private String content;
}
