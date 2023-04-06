package com.any.sys.mapper;

import com.any.sys.entity.User;
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
public interface UserMapper extends BaseMapper<User> {

    List<String> getRoleNameByUserId(@Param("userId") Integer userId);

}
