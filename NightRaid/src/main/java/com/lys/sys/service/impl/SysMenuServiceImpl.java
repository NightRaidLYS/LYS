package com.lys.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lys.annotation.SystemLog;
import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysMenu;
import com.lys.sys.mapper.SysMenuMapper;
import com.lys.sys.model.ShowMenu;
import com.lys.sys.model.SysMenuQueryDTO;
import com.lys.sys.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author lys
 * @since 2023-09-19
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public PageUtils listPage(SysMenuQueryDTO dto) {
        //查询以及菜单
        QueryWrapper<SysMenu> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",0)//查询所有一级菜单
                .like(StringUtils.isBlank(dto.getLabel()),"label",dto.getLabel())
                .orderByAsc("order_num");
        Page<SysMenu> page = this.page(dto.page(), wrapper);
        //查询出一级菜单下面的二级菜单
        List<SysMenu> list = page.getRecords();
        List<SysMenu> menus = list.stream().map(item -> {
            Long menuId = item.getMenuId();
            //判断当前菜单是否可以被删除
            int count = sysMenuMapper.canBeDeleted(menuId);
            if(count==0){
                item.setCanBeDeleted(true);
            }else {
                item.setCanBeDeleted(false);
            }
            //根据menuId查询出所有的二级菜单
            QueryWrapper<SysMenu> wapper1=new QueryWrapper<>();
            wapper1.eq("parent_id",menuId)
                    .orderByAsc("order_num");
            List<SysMenu> subMenus = this.baseMapper.selectList(wapper1);
            for (SysMenu subMenu : subMenus) {
                //判断子菜单是否被分配
                int i = sysMenuMapper.canBeDeleted(subMenu.getMenuId());
                if(i==0){
                    subMenu.setCanBeDeleted(true);
                }else {
                    subMenu.setCanBeDeleted(false);
                }
            }
            item.setChildren(subMenus);
            return item;
        }).collect(Collectors.toList());
        page.setRecords(menus);
        return new PageUtils(page);


    }

    @Override
    public List<SysMenu> listParent() {
        List<SysMenu> list = this.baseMapper.selectList(new QueryWrapper<SysMenu>().eq("parent_id", 0));
        return list;
    }
    @SystemLog("菜单更新或添加")
    @Override
    public void saveOrUpdateMenu(SysMenu menu) {
        if(menu.getMenuId()>0){
            //更新
            this.updateById(menu);
        }else {
            //添加
            if(StringUtils.isNotBlank(menu.getLabel())&&StringUtils.isBlank(menu.getName())){
                menu.setName(menu.getLabel());
            }
            if (menu.getParentId()==null){
                menu.setParentId(0l);
            }
            this.save(menu);
        }
    }

    @Override
    public SysMenu queryMenuById(Long menuId) {

        return this.baseMapper.selectById(menuId);
    }

    @SystemLog("菜单删除")
    @Override
    public String deleteMenu(Long menuId) {
        //判断数据能否删除
        int count=sysMenuMapper.canBeDeleted(menuId);
        if(count==0){
            //数据可以删除
            this.baseMapper.deleteById(menuId);
            return "1";
        }
        return "0";

    }

    @Override
    public List<Integer> queryMenuByRoleId(Long roleId) {
        return sysMenuMapper.queryMenuByRoleId(roleId);
    }

    @Override
    public List<ShowMenu> getShowMenu() {
        //获取当前登录的用户
        UsernamePasswordAuthenticationToken token= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName= (String) token.getPrincipal();
        //根据当前用户查询其具备的菜单
        List<SysMenu> list=this.baseMapper.selectShowMenuParentByUserName(userName);
        if (list!=null &&list.size()>0){
            List<ShowMenu> collect = list.stream().map(item -> {
                ShowMenu showMenu = new ShowMenu();
                showMenu.setIcon(item.getIcon());
                showMenu.setPath(item.getPath());
                showMenu.setLabel(item.getLabel());
                showMenu.setUrl(item.getUrl());
                showMenu.setName(item.getName());
                //获取子菜单
                List<SysMenu> subList = this.baseMapper.selectShowMenuSubByUserName(userName, item.getMenuId());
                if(subList!=null&&subList.size()>0){
                    List<ShowMenu> showMenus = new ArrayList<>();
                    for (SysMenu sysMenu : subList) {
                        ShowMenu subMenu = new ShowMenu();
                        subMenu.setIcon(sysMenu.getIcon());
                        subMenu.setPath(sysMenu.getPath());
                        subMenu.setLabel(sysMenu.getLabel());
                        subMenu.setUrl(sysMenu.getUrl());
                        subMenu.setName(sysMenu.getName());
                        showMenus.add(subMenu);
                    }
                    showMenu.setChildren(showMenus);
                }
                return showMenu;
            }).collect(Collectors.toList());
            return collect;
        }

        return null;
    }
}
