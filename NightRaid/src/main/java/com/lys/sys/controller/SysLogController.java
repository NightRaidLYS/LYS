package com.lys.sys.controller;

import com.lys.common.util.PageUtils;
import com.lys.sys.model.SysLogQueryDTO;
import com.lys.sys.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author lys
 * @since 2023-08-12
 */
@CrossOrigin
@Controller
@RequestMapping("/sys/sysLog")
public class SysLogController {
    @Autowired
    private ISysLogService sysLogService;

    @GetMapping("/list")
    public PageUtils list(SysLogQueryDTO dto){
        PageUtils pageUtils = sysLogService.listPage(dto);
        return pageUtils;
    }
}
