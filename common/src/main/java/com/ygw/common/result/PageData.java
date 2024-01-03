package com.ygw.common.result;

import com.ygw.common.constant.CommonConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author ygw
 * @createDate 2023/12/19 22:30
 **/
@Data
@NoArgsConstructor
public class PageData {
    private Integer totalCount;
    private Integer totalPage;
    private Integer currentPage;
    private Integer pageSize;

    public PageData(Integer totalCount, Integer currentPage, Integer pageSize) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        if (Objects.isNull(totalCount) || CommonConstant.NumConstant.INT_ZERO.equals(totalCount)){
            this.totalPage = CommonConstant.NumConstant.INT_ZERO;
        } else {
            this.totalPage = (int) Math.ceil((double) totalPage / pageSize);
        }
    }
}
