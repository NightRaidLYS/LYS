package com.lys.sys.controller;

import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysRole;
import com.lys.sys.model.SysRoleQueryDTO;
import com.lys.sys.service.ISysRoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/sysRole")
public class SysRoleController {
    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "查询分页角色",notes = "角色信息")
    @GetMapping("/list")
    public PageUtils list(@ApiParam(value = "查询的条件") SysRoleQueryDTO queryDTO){
        return roleService.queryPage(queryDTO);
    }

    @ApiOperation(value = "查询所有角色信息",notes = "角色信息")
    @GetMapping("/listAll")
    public List<SysRole> list(){
        return roleService.list();
    }

    @ApiOperation(value = "添加角色",notes = "添加角色")
    @PostMapping("/save")
    public String save(@RequestBody SysRole sysRole){
        roleService.saveOrUpdateRole(sysRole);

        return "success";
    }

    @GetMapping("/deleteRole")
    public String deleteRole(Long roleId){
        boolean flag = roleService.deleteRoleById(roleId);
        return flag?"1":"0";
    }

    /**
     * 检查角色名称是否存在
     * @param roleName
     * @return
     *
     */
    @ApiOperation(value = "检查角色名称是否存储",notes = "校验角色名称")
    @GetMapping("/checkRoleName")
    public String checkRoleName(String roleName){
        boolean flag = roleService.checkRoleName(roleName);
        return flag?"success":"fail";
    }
    //查询分配菜单信息
    @GetMapping("/dispatherRoleMenu")
    public Map<String,Object> dispatherRoleMenu(Long roleId){

        return  roleService.dispatherRoleMenu(roleId);
    }
}
