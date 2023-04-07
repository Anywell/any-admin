package com.any.sys.mapper;

import com.any.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Integer> getMenuIdListByRoleId(@Param("roleId") Integer roleId);

}
