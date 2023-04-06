package com.any.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.any.sys.entity.User;
import com.any.sys.mapper.UserMapper;
import com.any.sys.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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
            UUID uuid = UUID.randomUUID();
            String key = "user:" + uuid;

            // 存入redis
            loginUser.setPassword(null);
            ops.set(key, loginUser, 30, TimeUnit.MINUTES);

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", key);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null){
            // 反序列化
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());
            // 角色
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles", roleList);

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }
}
