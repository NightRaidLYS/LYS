package com.lys.sys.service.impl;

import com.lys.sys.entity.SysLog;
import com.lys.sys.entity.SysRole;
import com.lys.sys.entity.SysUser;
import com.lys.sys.service.ISysRoleService;
import com.lys.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    ISysUserService sysUserService;

    @Autowired
    ISysRoleService sysRoleService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1.需要根据账号查询
        List<SysUser> list = sysUserService.queryByUserName(username);
        if(list != null && list.size() == 1){
            // 账号是存在的
            SysUser sysUser = list.get(0);
            // 根据当前登录的账号查询到关联的角色信息
            List<SysRole> sysRoles = sysRoleService.queryByUserId(sysUser.getUserId());
            List<GrantedAuthority> listRole = new ArrayList<>();
            if(sysRoles != null && sysRoles.size() > 0){
                for (SysRole sysRole : sysRoles) {
                    listRole.add(new SimpleGrantedAuthority(sysRole.getRoleName()));
                }
            }
            // 密码模拟的是就数据库查询出来
            return new User(sysUser.getUsername(),sysUser.getPassword(),listRole);
        }
        return null;
    }

}
