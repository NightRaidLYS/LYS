package com.lys.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;


/**
 * <p>
 * 
 * </p>
 *
 * @author lys
 * @since 2023-11-23
 */
@TableName("cus_customer")
@ApiModel(value = "CusCustomer对象", description = "")
public class CusCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    private String customerName;

    private String phone;

    private String customerType;

    private String address;

    @TableField(exist = false)
    private Boolean display;//表示分配按钮是否显示

    private Integer status;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CusCustomer{" +
            "customerId=" + customerId +
            ", customerName=" + customerName +
            ", phone=" + phone +
            ", customerType=" + customerType +
        "}";
    }
}
