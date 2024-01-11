package com.lys.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lys.common.util.PageUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lys.common.util.PinyinUtils;
import com.lys.sys.entity.CusCustomer;
import com.lys.sys.mapper.CusCustomerMapper;
import com.lys.sys.mapper.SysUserMapper;
import com.lys.sys.model.CusCustomerDTO;
import com.lys.sys.model.CusCustomerQueryDTO;
import com.lys.sys.model.ShowAddress;
import com.lys.sys.service.ICusCustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lys
 * @since 2023-11-23
 */
@Service
public class CusCustomerServiceImpl extends ServiceImpl<CusCustomerMapper, CusCustomer> implements ICusCustomerService {

    @Autowired
    private CusCustomerMapper customerMapper;
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public PageUtils queryPage(CusCustomerQueryDTO dto) {
        QueryWrapper<CusCustomer> wrapper = getCusCustomerQueryWrapper(dto);
        wrapper.eq("status",1);
        Object username = getUsername();
        //判断当前用户是否是超级管理员
        int count=this.baseMapper.display(username);

        Page<CusCustomer> page = this.page(dto.page(), wrapper);

        List<CusCustomer> records = page.getRecords();
        boolean dis=count>=1;
        records.stream().forEach(item->{
            //若是则让前端显示分配按钮
            item.setDisplay(dis);
        });
        return new PageUtils(page);

    }


    @Override
    public void saveOrUpdateCustomer(CusCustomer customer) {
        if(customer.getCustomerId()!=null && customer.getCustomerId()>0){
            this.baseMapper.updateById(customer);
        }else {
            customer.setStatus(0);
            this.baseMapper.insert(customer);
        }
    }

    @Override
    public CusCustomer queryCustomerById(Long customerId) {
        CusCustomer customer = this.baseMapper.selectById(customerId);
        return customer;
    }

    @Override
    public boolean deleteCustomer(Long customerId) {
        String type=customerMapper.selectByType(customerId);
        if(!"VIP客户".equals(type)){
            this.baseMapper.deleteById(customerId);
        }else {
            return false;
        }
        return true;
    }

    @Override
    public List<ShowAddress> getAddress() {
        List<CusCustomer> list = this.baseMapper.selectList(null);

        return getShowAddresses(list);
    }

    @Override
    public CusCustomerDTO queryCustomerAndUsers(Long customerId) {
        String username=customerMapper.selectCustomerUser(customerId);
        String customerName = customerMapper.selectById(customerId).getCustomerName();
        List<String> users = userMapper.selectAllUserName();
        return new CusCustomerDTO(username,customerId,customerName,users);
    }

    @Override
    public PageUtils queryMyCustomer(CusCustomerQueryDTO dto) {
        QueryWrapper<CusCustomer> wrapper = getCusCustomerQueryWrapper(dto);
        Object username = getUsername();

        List<CusCustomer> myCustomer=customerMapper.queryMyCustomer(username);
        wrapper.in("customer_id",myCustomer.stream().map(CusCustomer::getCustomerId).collect(Collectors.toList()));
        Page<CusCustomer> page = this.page(dto.page(), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<ShowAddress> getMyCustomerAddress() {
        Object username = getUsername();
        List<CusCustomer> myCustomer = customerMapper.queryMyCustomer(username);
        return getShowAddresses(myCustomer);
    }

    //管理员分配人员
    @Override
    public boolean distribution(CusCustomerDTO dto) {
        //先查询所分配的人是否已经拥有了此段数据，先查询分配人员的id
        Long userId=userMapper.selectUserIdByUserName(dto.getUser());
        //再根据分配人员的id与customerId查询该记录是否已经存在
        Long count=customerMapper.selectCountUserAndCustomer(userId,dto.getCustomerId());
        if(count==1){
            //说明已经存在此数据，不需要添加
            return false;
        }else {
            //添加或者更新
            SaveOrUpdateUserAndCustomer(userId,dto);
            return true;
        }
    }

    @Override
    public PageUtils queryCommonCustomer(CusCustomerQueryDTO dto) {
        QueryWrapper<CusCustomer> wrapper = getCusCustomerQueryWrapper(dto);
        //查询公海客户列表信息，并添加到wrapper中
        List<Long> list=customerMapper.selectCommonCustomer();
        if (list!=null&&!list.isEmpty()){
            wrapper.in("customer_id",list);
        }else {
            PageUtils pageUtils = new PageUtils(new Page<>(dto.getPageIndex(), dto.getPageSize()));
            return pageUtils;
        }
        Page<CusCustomer> page=this.page(dto.page(),wrapper);
        return new PageUtils(page);

    }

    @Override
    public List<ShowAddress> getCummonCustomerAddress() {
        QueryWrapper<CusCustomer> wrapper = new QueryWrapper<>();
        wrapper.eq("status",0);
        List<CusCustomer> list = this.baseMapper.selectList(wrapper);
        if (list!=null&&list.size()>0){
            List<ShowAddress> collect=list.stream().map(item->{
                ShowAddress address = new ShowAddress();
                address.setLabel(item.getAddress());
                String toPinyin = PinyinUtils.convertToPinyinWithoutTone(item.getAddress());
                address.setValue(toPinyin);
                return address;
            }).distinct().collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    public boolean addCommonCustomer(Long customerId) {
        //获取当前登录的用户
        Object username = getUsername();
        CusCustomer list=customerMapper.selectById(customerId);
        list.setStatus(1);
        customerMapper.updateById(list);
        //根据当前登录的用户，将数据插入
        Long userIdByUserName = userMapper.selectUserIdByUserName(username.toString());
        customerMapper.insertIntoUserAndCustomer(userIdByUserName,list.getCustomerId());
        return true;


    }

    private void SaveOrUpdateUserAndCustomer(Long userId, CusCustomerDTO dto) {
        //判断当的传递过来的username是否为空
        if(dto.getUsername()==null||dto.getUsername().equals("")){
            //添加操作
            customerMapper.insertIntoUserAndCustomer(userId,dto.getCustomerId());
        }else {
            //根据传递过来的customerID去修改他的userId
            customerMapper.updateByCustomerId(userId,dto.getCustomerId());
        }
    }

    private List<ShowAddress> getShowAddresses(List<CusCustomer> myCustomer) {
        if (myCustomer!=null&&myCustomer.size()>0){
            List<ShowAddress> collect=myCustomer.stream().map(item->{
                ShowAddress address = new ShowAddress();
                address.setLabel(item.getAddress());
                String toPinyin = PinyinUtils.convertToPinyinWithoutTone(item.getAddress());
                address.setValue(toPinyin);
                return address;
            }).distinct().collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    private Object getUsername() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Object username = authentication.getPrincipal();
        return username;
    }

    private QueryWrapper<CusCustomer> getCusCustomerQueryWrapper(CusCustomerQueryDTO dto) {
        QueryWrapper<CusCustomer> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getCustomerName()),"customer_name", dto.getCustomerName());
        wrapper.like(StringUtils.isNotEmpty(dto.getAddress()),"address", dto.getAddress());
        return wrapper;
    }


}
