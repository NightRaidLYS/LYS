package com.lys.sys.controller;

import com.lys.common.constant.SystemConstant;
import com.lys.common.util.PageUtils;
import com.lys.sys.model.CusCustomerDTO;
import com.lys.sys.model.CusCustomerQueryDTO;
import com.lys.sys.model.ShowAddress;
import com.lys.sys.service.ICusCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cus/myCustomer")
public class MyCustomerController {

    @Autowired
    private ICusCustomerService customerService;

    @RequestMapping("/list")
    public PageUtils listAll(CusCustomerQueryDTO dto){
        return customerService.queryMyCustomer(dto);
    }
    @RequestMapping("/address")
    public List<ShowAddress> address(){
        return customerService.getMyCustomerAddress();    }
    @GetMapping("/allocation")
    public CusCustomerDTO queryCustomerAndUsers(Long customerId){
        return customerService.queryCustomerAndUsers(customerId);
    }
    @PostMapping("/distribution")
    public String Distribution(@RequestBody CusCustomerDTO dto){
        boolean flag=customerService.distribution(dto);
        return flag? SystemConstant.CHECK_SUCCESS:SystemConstant.CHECK_FAIL;
    }
}
