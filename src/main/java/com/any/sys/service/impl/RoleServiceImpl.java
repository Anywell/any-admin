package com.any.sys.service.impl;

import com.any.sys.entity.Role;
import com.any.sys.entity.RoleMenu;
import com.any.sys.mapper.RoleMapper;
import com.any.sys.mapper.RoleMenuMapper;
import com.any.sys.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        // 1. 写入角色表
        this.baseMapper.insert(role);
        // 2. 写入菜单跟角色对应表
        if (role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
               roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer roleId) {

        Role role = this.baseMapper.selectById(roleId);
        List<Integer> menuList = roleMenuMapper.getMenuIdListByRoleId(roleId);
        role.setMenuIdList(menuList);
        return role;
    }

    @Override
    @Transactional
    public void updateRoleById(Role role) {
        // 1.修改角色表
        this.baseMapper.updateById(role);
        // 2.删除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);
        // 3.新增权限
        if (role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public void removeRoleById(Integer id) {
        // 1.删除角色表数据
        this.baseMapper.deleteById(id);
        // 2. 删除权限表
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
    }
}
