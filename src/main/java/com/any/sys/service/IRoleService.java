package com.any.sys.service;

import com.any.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer id);

    void updateRoleById(Role role);

    void removeRoleById(Integer id);
}
