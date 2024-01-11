package com.lys.sys.controller;

import com.lys.common.constant.SystemConstant;
import com.lys.sys.entity.CusCustomer;
import com.lys.sys.model.ShowAddress;
import com.lys.sys.service.ICusCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cus/CommonCustomer")
public class CommonCustomer {
    @Autowired
    private ICusCustomerService customerService;
    @RequestMapping("/address")
    public List<ShowAddress> address(){
        return customerService.getCummonCustomerAddress();    }
    @PostMapping("/add")
    public String addCommonCustomer(Long customerId){
        customerService.addCommonCustomer(customerId);
        return SystemConstant.CHECK_SUCCESS;

    }
}
