package com.any.sys.controller;

import com.any.common.vo.Result;
import com.any.sys.entity.Role;
import com.any.sys.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@Api(tags = "角色列表接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;


    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> getRoleList(@RequestParam(value = "roleName", required = false) String roleName,
                                                   @RequestParam(value = "pageNo", defaultValue="1") Long pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize){

        LambdaQueryWrapper<Role> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName), Role::getRoleName, roleName);

        Page<Role> page = new Page<>(pageNo, pageSize);
        roleService.page(page, wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        log.debug(data.toString());

        return Result.success(data);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @ApiOperation("添加角色接口")
    @PostMapping
    public Result<Map<String, Object>> addRole(@RequestBody Role role){

       roleService.addRole(role);
       return Result.success("角色添加成功");

    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @ApiOperation("修改角色接口")
    @PutMapping
    public Result<Map<String, Object>> updateRole(@RequestBody Role role) {
        roleService.updateRoleById(role);
        return Result.success("角色修改成功");
    }
    /**
     * 修改角色
     * @param id
     * @return
     */
    @ApiOperation("根据id修改角色")
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){

        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ApiOperation("删除角色接口")
    @DeleteMapping("/{id}")
    public Result<?> deleteRoleById(@PathVariable("id") Integer id){
       roleService.removeRoleById(id);
       return Result.success("角色删除成功");
    }

    @ApiOperation("获取所有角色列表")
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }

}
