package com.lys.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lys.annotation.SystemLog;
import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysMenu;
import com.lys.sys.entity.SysRole;
import com.lys.sys.mapper.SysRoleMapper;
import com.lys.sys.model.SysRoleQueryDTO;
import com.lys.sys.service.ISysMenuService;
import com.lys.sys.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysMenuService menuService;

    @Override
    public PageUtils queryPage(SysRoleQueryDTO queryDTO) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().like(
                StringUtils.isNotEmpty(queryDTO.getRoleName()),"role_name",queryDTO.getRoleName()
        );
        Page<SysRole> page = this.page(queryDTO.page(), wrapper);

        return new PageUtils(page);
    }

    @SystemLog("添加或更新角色")
    @Override
    @Transactional
    public void saveOrUpdateRole(SysRole role) {
        //同步维护角色和菜单的关系
        // 判断 角色编号是否存在，如果存在就走更新的逻辑否则新增数据
        if(role.getRoleId() != null && role.getRoleId() != 0){
            // 表示更新操作
            this.update(role);
            //根据角色id，删除分配的菜单信息
            sysRoleMapper.deleteMenuByRoleId(role.getRoleId());

        }else{
            this.saveRole(role);
        }
        //新增分配的菜单信息
        List<Integer> menuIds = role.getMenuIds();
        if(menuIds!=null&& menuIds.size()>0){
            //分配有相关信息
            for (Integer menuId : menuIds) {
                sysRoleMapper.insertRoleAndMenu(role.getRoleId(),menuId);
            }
        }
    }

    public void saveRole(SysRole role){
        role.setCreateTime(LocalDateTime.now());
        this.save(role);
    }

    @Override
    public void update(SysRole role) {
        this.baseMapper.updateById(role);
    }

    @Override
    public void deleteBatch(Long[] roleIds) {

    }

    /**
     *
     * @param roleName
     * @return
     *    true 存在
     *    false 不存在
     */
    @Override
    public boolean checkRoleName(String roleName) {
        if(StringUtils.isEmpty(roleName)){
            return false;
        }
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().eq("role_name",roleName);
        int count = this.count(queryWrapper);
        return count > 0;
    }

    @SystemLog("删除角色")
    @Override
    public boolean deleteRoleById(Long roleId) {
        // 删除角色信息
        // 如果这个角色分配给了 用户或者角色绑定了菜单。那么都不允许删除角色
        // 查看该角色是否分配给了用户
        int count = this.baseMapper.checkRoleCanDelete(roleId);
        if(count == 0){
            // 表示可以删除
            this.baseMapper.deleteById(roleId);
        }
        return count == 0;
    }

    @Override
    public List<SysRole> queryByUserId(Long userId) {
        return sysRoleMapper.queryByUserId(userId);
    }

    @Override
    public Map<String, Object> dispatherRoleMenu(Long roleId) {
        //查询出所有的菜单信息
        List<SysMenu> parent = menuService.listParent();
        List<Map<String,Object>> list=new ArrayList<>();
        if(parent!=null&&parent.size()>0){
            for (SysMenu menu : parent) {
                Map<String,Object> map=new HashMap<>();
                map.put("id",menu.getMenuId());
                map.put("label",menu.getName());
                //根据父菜单编号查询到对应的子菜单信息
                Long parentId = menu.getMenuId();
                QueryWrapper<SysMenu> wrapper=new QueryWrapper<>();
                wrapper.eq("parent_id",parentId);
                List<SysMenu> subMenus = menuService.list(wrapper);
                List<Map<String,Object>> subList=new ArrayList<>();
                if (subMenus!=null&&subMenus.size()>0){
                    for (SysMenu subMenu : subMenus) {
                        Map<String,Object> subMap=new HashMap<>();
                        subMap.put("id",subMenu.getMenuId());
                        subMap.put("label",subMenu.getName());
                        subList.add(subMap);
                    }
                }
                //父子菜单关联
                map.put("children",subList);
                list.add(map);
            }

        }
        //根据角色编号查询分配菜单编号
        List<Integer> menuIds=menuService.queryMenuByRoleId(roleId);
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("checks",menuIds);
        resMap.put("treeData",list);
        return resMap;


    }
}
