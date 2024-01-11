package com.lys.sys.model;

import com.lys.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CusCustomerQueryDTO extends PageDTO {
    @ApiModelProperty("客户名称")
    private String customerName;
    private String address;
}
