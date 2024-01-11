package com.lys.sys.model;

import com.lys.sys.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//更新数据的dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateDTO {
    private List<SysMenu> parents;
    private SysMenu menu;
}
