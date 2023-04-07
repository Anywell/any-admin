package com.any.sys.service;

import com.any.sys.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenuList();

    List<Menu> getMenuListByUserId(Integer userId);
}
