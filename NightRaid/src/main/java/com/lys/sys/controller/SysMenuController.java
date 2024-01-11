package com.lys.sys.controller;

import com.lys.common.constant.SystemConstant;
import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysMenu;
import com.lys.sys.model.MenuUpdateDTO;
import com.lys.sys.model.ShowMenu;
import com.lys.sys.model.SysMenuQueryDTO;
import com.lys.sys.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author lys
 * @since 2023-09-19
 */
@Controller
@RequestMapping("/sys/sysMenu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    @GetMapping("/list")
    public PageUtils list(SysMenuQueryDTO dto){
        return sysMenuService.listPage(dto);
    }

    @GetMapping("/listParent")
    public List<SysMenu> listParent(){

        return  sysMenuService.listParent();
    }
    @PostMapping("/save")
    public String save(@RequestBody SysMenu menu){
        if(menu!=null){
            sysMenuService.saveOrUpdateMenu(menu);
        }
        return SystemConstant.CHECK_SUCCESS;
    }

    @GetMapping("/queryMenuById")
    public MenuUpdateDTO queryMenuById(Long menuId){
        SysMenu sysMenu = sysMenuService.queryMenuById(menuId);
        List<SysMenu> parents = sysMenuService.listParent();
        return new MenuUpdateDTO(parents,sysMenu);
    }

    @GetMapping("/deleteMenu")
    public String deleteMenu(Long menuId){
        //不能删除返回0，否则返回1
    return sysMenuService.deleteMenu(menuId);
    }

    @GetMapping("/getShowMenu")
    public List<ShowMenu> getShowMenu(){
        return sysMenuService.getShowMenu();
    }

}
