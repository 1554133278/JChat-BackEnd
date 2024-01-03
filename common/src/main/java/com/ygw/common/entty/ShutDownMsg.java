package com.ygw.common.entty;

import lombok.Data;

@Data
public class ShutDownMsg extends NettyMsg {

    {
        setType(6);
    }
}
