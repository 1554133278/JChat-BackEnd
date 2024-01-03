package com.ygw.user.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ygw
 * @createDate 2023/12/19 22:57
 **/
@Getter
public enum FriendStatusEnum {
    NORMAL(0, "normal"),
    BE_SHIELDED(1, "be shielded"),
    BE_DELETE(10, "be deleted"),
    ;
    private final Integer code;
    private final String value;

    FriendStatusEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

}
