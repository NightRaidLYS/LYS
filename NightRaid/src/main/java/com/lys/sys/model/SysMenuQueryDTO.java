package com.lys.sys.model;

import com.lys.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMenuQueryDTO extends PageDTO {
    @ApiModelProperty("查询字段")
    private String label;
}
