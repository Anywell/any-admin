package com.any.sys.service.impl;

import com.any.sys.entity.Menu;
import com.any.sys.mapper.MenuMapper;
import com.any.sys.service.IMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getMenuList() {

        // 1.查询一级菜单
        LambdaQueryWrapper<Menu> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = this.list(wrapper);
        // 2.填充一级菜单子菜单
        setMenuChildren(menuList);
        return menuList;
    }

    private void setMenuChildren(List<Menu> menuList) {
        if (menuList != null){
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subWrapper =  new LambdaQueryWrapper<>();
                subWrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> subMenuList = this.list(subWrapper);
                menu.setChildren(subMenuList);
                // 递归查询
                setMenuChildren(subMenuList);
            }
        }
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId){

        // 1.查询一级菜单
        List<Menu> menuList = this.baseMapper.getMenuListByUserId(userId, 0);
        // 2. 处理子菜单
        setMenuChilrenByUserId(userId, menuList);
        return menuList;
    }

    private void setMenuChilrenByUserId(Integer userId, List<Menu> menuList) {
        if (menuList != null){
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.baseMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归获取
                setMenuChilrenByUserId(userId, subMenuList);
            }
        }
    }

}
