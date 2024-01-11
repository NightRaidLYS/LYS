package com.lys.sys.service;

import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lys.sys.model.SysUserQueryDTO;

import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysUser> queryByUserName(String username);

    PageUtils queryPage(SysUserQueryDTO dto);

    boolean checkUserName(String username);

    void saveOrUpdateUser(SysUser sysUser);

    SysUser queryByUserId(Long userId);
}
