package com.any.sys.controller;

import com.any.common.vo.Result;
import com.any.sys.entity.User;
import com.any.sys.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
@Api(tags = {"用户接口列表"})
@RestController
@RequestMapping("/user")
//@CrossOrigin  // 跨域处理，使用另一种方式配置全局跨域处理
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation("获取所有用户信息，未使用，使用的是list接口")
    @GetMapping("/all")
    public Result<List<User>> getAll(){

        List<User> list = userService.list();
        return Result.success("查询成功", list);
    }

    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user){

        Map<String, Object> data =  userService.login(user);
        if (data != null){
            // 登录成功
            return Result.success(data);
        }
        return Result.fail(20002, "用户名或密码错误！");
    }

    @ApiOperation("用户信息展示接口")
    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token){
        // 根据token获取用户信息，redis
        Map<String,Object> data =  userService.getUserInfo(token);
        if (data != null){
            return Result.success(data);
        }
        return Result.fail(20003, "登录信息无效，请重新登录");
    }

    @ApiOperation("用户退出接口")
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    @ApiOperation("用户查询接口")
    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username", required = false) String username,
                                              @RequestParam(value = "phone", required = false) String phone,
                                              @RequestParam(value = "pageNo") Long pageNo,
                                              @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        wrapper.eq(StringUtils.hasLength(phone), User::getPhone, phone);

        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @ApiOperation("用户添加接口")
    @PostMapping
    public Result<?> addUser(@RequestBody User user){
        // 加密密码
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);

        userService.addUser(user);
        return Result.success("新增用户成功");

    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @ApiOperation("用户修改接口")
    @PutMapping
    public Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);
        userService.updateUserById(user);
        return Result.success("用户修改成功");
    }

    /**
     * 根据id查找用户信息
     * @param id
     * @return
     */
    @ApiOperation("根据id查询用户接口")
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    @ApiOperation("根据id删除用户接口")
    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer id) {
        userService.removeById(id);
        return Result.success("用户删除成功");
    }

}
