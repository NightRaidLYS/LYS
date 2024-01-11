package com.lys.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lys.sys.entity.CusCustomer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lys
 * @since 2023-11-23
 */
@Mapper
public interface CusCustomerMapper extends BaseMapper<CusCustomer> {

    String selectByType(Long customerId);

    int display(Object username);

    String selectCustomerUser(Long customerId);

    List<CusCustomer> queryMyCustomer(Object username);

    Long selectCountUserAndCustomer(Long userId, Long customerId);

    void insertIntoUserAndCustomer(Long userId, Long customerId);

    void updateByCustomerId(Long userId, Long customerId);

    List<Long> selectCommonCustomer();
}
