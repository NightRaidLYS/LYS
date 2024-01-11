package com.lys.sys.service;

import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lys.sys.model.ShowMenu;
import com.lys.sys.model.SysMenuQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author lys
 * @since 2023-09-19
 */
public interface ISysMenuService extends IService<SysMenu> {

    PageUtils listPage(SysMenuQueryDTO dto);

    List<SysMenu> listParent();

    void saveOrUpdateMenu(SysMenu menu);

    SysMenu queryMenuById(Long menuId);

    String deleteMenu(Long menuId);

    List<Integer> queryMenuByRoleId(Long roleId);


    List<ShowMenu> getShowMenu();
}
