package com.lys.sys.mapper;

import com.lys.sys.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author lys
 *
 * @since 2023-09-19
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    int canBeDeleted(Long menuId);

    List<Integer> queryMenuByRoleId(@Param("roleId") Long roleId);

    List<SysMenu> selectShowMenuParentByUserName(@Param("userName") String userName);

    List<SysMenu> selectShowMenuSubByUserName(@Param("userName") String userName, @Param("menuId") Long menuId);
}
