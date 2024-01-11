package com.lys.sys.service;

import com.lys.common.util.PageUtils;
import com.lys.sys.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lys.sys.model.SysLogQueryDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author lys
 * @since 2023-08-12
 */
public interface ISysLogService extends IService<SysLog> {
    PageUtils listPage(SysLogQueryDTO dto);

}
