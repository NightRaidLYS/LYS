package com.lys.sys.mapper;

import com.lys.sys.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author lys
 * @since 2023-08-11
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    void deleteRoleByUserId(Long userId);

    void saveUserAndRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    List<Integer> selectRoleIdsByUserId(@Param("userId") Long userId);

    List<String> selectAllUserName();

    Long selectUserIdByUserName(String user);
}
