package com.any.sys.service.impl;

import com.any.sys.entity.Role;
import com.any.sys.mapper.RoleMapper;
import com.any.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
