package com.lys.sys.model;

import com.lys.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysRoleQueryDTO extends PageDTO {
    @ApiModelProperty("角色名称")
    private String roleName;
}
