package com.lys.sys.controller;

import com.lys.common.constant.SystemConstant;
import com.lys.common.util.PageUtils;


import com.lys.sys.entity.CusCustomer;
import com.lys.sys.model.CusCustomerDTO;
import com.lys.sys.model.CusCustomerQueryDTO;
import com.lys.sys.model.ShowAddress;
import com.lys.sys.service.ICusCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lys
 * @since 2023-11-23
 */
@RestController
@RequestMapping("/cus/cusCustomer")
public class CusCustomerController {

    @Autowired
    private ICusCustomerService customerService;

    @GetMapping("/list")
    public PageUtils list(CusCustomerQueryDTO dto){
        return customerService.queryPage(dto);
    }

    @GetMapping("/commonCustomerList")
    public PageUtils listCommonCustomer(CusCustomerQueryDTO dto){
        return customerService.queryCommonCustomer(dto);
    }

    @PostMapping("/save")
    public String save(@RequestBody CusCustomer customer){
        customerService.saveOrUpdateCustomer(customer);
        return "success";
    }
    @GetMapping("/queryCustomerById")
    public CusCustomer queryCustomerById(Long customerId){
        return customerService.queryCustomerById(customerId);
    }

    @GetMapping("/delete")
    public String deleteCustomer(Long customerId){
        boolean flag=customerService.deleteCustomer(customerId);
        return flag?"1":"0";
    }
    @GetMapping("/address")
    public List<ShowAddress> getAddress(){
        return customerService.getAddress();
    }

//    @GetMapping("/allocation")
//    public CusCustomerDTO queryCustomerAndUsers(Long customerId){
//        return customerService.queryCustomerAndUsers(customerId);
//    }
//    @PostMapping("/distribution")
//    public String Distribution(@RequestBody CusCustomerDTO dto){
//        boolean flag=customerService.distribution(dto);
//        return flag? SystemConstant.CHECK_SUCCESS:SystemConstant.CHECK_FAIL;
//    }
}
