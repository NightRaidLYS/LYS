package com.lys.sys.model;

import com.lys.common.model.PageDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserQueryDTO extends PageDTO {
    @ApiModelProperty("用户名称")
    private String username;
}
