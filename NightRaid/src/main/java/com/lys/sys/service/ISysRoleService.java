package com.lys.sys.service;

import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lys.sys.model.SysRoleQueryDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
public interface ISysRoleService extends IService<SysRole> {
    PageUtils queryPage(SysRoleQueryDTO queryDTO);

    void saveOrUpdateRole(SysRole role);

    void update(SysRole role);

    void deleteBatch(Long[] roleIds);

    boolean checkRoleName(String roleName);

    boolean deleteRoleById(Long roleId);

    List<SysRole> queryByUserId(Long userId);

    Map<String, Object> dispatherRoleMenu(Long roleId);
}
