package com.ygw.user.enums;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ygw
 * @createDate 2023/12/19 22:54
 **/
@Getter
public enum ApplyStatusEnum {
    INIT(0, "apply init"),
    APPROVE(1, "approve apply"),
    REJECT(10, "reject apply"),
    ;
    private final Integer code;
    private final String value;

    ApplyStatusEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

}
