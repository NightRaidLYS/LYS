package com.lys.sys.model;

import com.lys.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysLogQueryDTO extends PageDTO {
    @ApiModelProperty("查询字段")
    private String msg;
}
