package com.any.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.any.common.vo.JwtUtil;
import com.any.sys.entity.Menu;
import com.any.sys.entity.User;
import com.any.sys.entity.UserRole;
import com.any.sys.mapper.UserMapper;
import com.any.sys.mapper.UserRoleMapper;
import com.any.sys.service.IMenuService;
import com.any.sys.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService menuService;


    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> login(User user) {

        ValueOperations ops = redisTemplate.opsForValue();

        // 1.根据用户名和密码查询
        LambdaQueryWrapper<User> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne((wrapper));
//        wrapper.eq(User::getPassword, user.getPassword());
        boolean matches = passwordEncoder.matches(user.getPassword(), loginUser.getPassword());
        // 2.结果不为null，且密码匹配，登录成功，生成一个token，并将用户信息存入redis
        if (loginUser != null && matches){
            // 登录成功，暂时使用UUID生成token，终极方案jwt
//            UUID uuid = UUID.randomUUID();
//            String key = "user:" + uuid;
            // 存入redis
//            loginUser.setPassword(null);
//            ops.set(key, loginUser, 30, TimeUnit.MINUTES);

            // 创建jwt
            String token = jwtUtil.createToken(loginUser);
            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
//        Object obj = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try {
            // 从token中获取用户信息
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginUser != null){
            // 反序列化
//          User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());
            // 角色
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles", roleList);

            // 菜单列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList", menuList);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
//        redisTemplate.delete(token);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        // 1.保存用户表
        this.baseMapper.insert(user);
        // 2.保存用户角色表
        addUserRole(user);
    }


    @Override
    public void updateUserById(User user) {
        // 1. 修改user表
        this.baseMapper.updateById(user);
        // 2. 删除旧的用户角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(wrapper);
        // 3. 更新的用户角色
        addUserRole(user);
    }

    @Override
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);
        // 查询用户角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);

        List<Integer> roleIdList = userRoles.stream().map(item -> {
            return item.getRoleId();
        }).collect(Collectors.toList());

        user.setRoleIdList(roleIdList);
        return user;
    }

    /**
     * 新增用户角色
     * @param user
     */
    private void addUserRole(User user) {
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }
}
