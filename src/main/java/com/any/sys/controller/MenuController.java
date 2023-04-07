package com.any.sys.controller;

import com.any.common.vo.Result;
import com.any.sys.entity.Menu;
import com.any.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author anywell
 * @since 2023-04-03
 */
@Api(tags = "菜单列表接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation("查询所有菜单数据 ")
    @GetMapping
    public Result<List<Menu>> getMenuList() {
        List<Menu> menuList =  menuService.getMenuList();
        return Result.success(menuList);
    }

}
