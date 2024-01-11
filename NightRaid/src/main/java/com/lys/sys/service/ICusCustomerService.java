package com.lys.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lys.common.util.PageUtils;
import com.lys.sys.entity.CusCustomer;
import com.lys.sys.model.CusCustomerDTO;
import com.lys.sys.model.CusCustomerQueryDTO;
import com.lys.sys.model.ShowAddress;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lys
 * @since 2023-11-23
 */
public interface ICusCustomerService extends IService<CusCustomer> {
    PageUtils queryPage(CusCustomerQueryDTO dto);
    void saveOrUpdateCustomer(CusCustomer customer);
    CusCustomer queryCustomerById(Long customerId);

    boolean deleteCustomer(Long customerId);

    List<ShowAddress> getAddress();

    CusCustomerDTO queryCustomerAndUsers(Long customerId);

    PageUtils queryMyCustomer(CusCustomerQueryDTO dto);

    List<ShowAddress> getMyCustomerAddress();

    boolean distribution(CusCustomerDTO dto);

    PageUtils queryCommonCustomer(CusCustomerQueryDTO dto);

    List<ShowAddress> getCummonCustomerAddress();

    boolean addCommonCustomer(Long id);
}
