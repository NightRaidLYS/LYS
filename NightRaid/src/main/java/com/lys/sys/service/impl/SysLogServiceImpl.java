package com.lys.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysLog;
import com.lys.sys.filter.ClientHostAddress;
import com.lys.sys.mapper.SysLogMapper;
import com.lys.sys.model.SysLogQueryDTO;
import com.lys.sys.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author lys
 * @since 2023-08-12
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    /**
     * 查询系统日志信息
     *    用户名或者用户操作
     * @param dto
     * @return
     */
    @Override
    public PageUtils listPage(SysLogQueryDTO dto) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<SysLog>();
        if(StringUtils.isNotEmpty(dto.getMsg())){
            System.out.println("---------->");
            wrapper.and(item->{
                item.like("username",dto.getMsg())
                        .or().like("operation",dto.getMsg());
            });
        }

        Page<SysLog> page = this.page(dto.page(), wrapper);
        return new PageUtils(page);
    }

}
