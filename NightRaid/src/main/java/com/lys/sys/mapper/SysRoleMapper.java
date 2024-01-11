package com.lys.sys.mapper;

import com.lys.sys.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    int checkRoleCanDelete(@Param("roleId") Long roleId);

    List<SysRole> queryByUserId(Long userId);

    void deleteMenuByRoleId(@Param("roleId") Long roleId);

    void insertRoleAndMenu(@Param("roleId") Long roleId, @Param("menuId") Integer menuId);
}
