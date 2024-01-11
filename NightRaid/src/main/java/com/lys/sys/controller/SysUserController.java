package com.lys.sys.controller;

import com.lys.common.constant.SystemConstant;
import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysRole;
import com.lys.sys.entity.SysUser;
import com.lys.sys.model.SysUserQueryDTO;
import com.lys.sys.service.ISysRoleService;
import com.lys.sys.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
@Api(tags = "系统用户",value = "SysUser")
@RestController
@RequestMapping("/sys/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "查询系统用户", notes = "查询用户")
    @GetMapping("/list")
    public PageUtils list(SysUserQueryDTO dto) {
        return userService.queryPage(dto);
    }
    @GetMapping("/checkUserName")
    public String checkUserName(String username){
    boolean flag=userService.checkUserName(username);
    return flag? SystemConstant.CHECK_SUCCESS:SystemConstant.CHECK_FAIL;
    }
    @PostMapping("/save")
    public String save(@RequestBody SysUser sysUser){
        userService.saveOrUpdateUser(sysUser);
        return "success";
    }
//    @GetMapping("/queryUserById")
//    public SysUser queryUserById(Long userId){
//        return userService.queryByUserId(userId);
//    }


    @GetMapping("/queryUserById")
    public Map<String,Object> queryUserById(Long userId){
        //根据id查询用户信息
        SysUser sysUser = userService.queryByUserId(userId);
//        查询所有的角色信息
        List<SysRole> roles = roleService.list();
        Map<String,Object> map=new HashMap<>();
        map.put("roles",roles);
        map.put("user",sysUser);
        return map;
    }
}
