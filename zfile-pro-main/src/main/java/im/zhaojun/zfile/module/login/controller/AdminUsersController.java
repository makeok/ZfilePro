package im.zhaojun.zfile.module.login.controller;

import cn.hutool.crypto.SecureUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import im.zhaojun.zfile.core.exception.ServiceException;
import im.zhaojun.zfile.core.util.AjaxJson;
import im.zhaojun.zfile.core.util.CodeMsg;
import im.zhaojun.zfile.module.login.model.entity.User;
import im.zhaojun.zfile.module.login.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户基础设置模块接口
 *
 * @author zhaojun
 */
@Api(tags = "用户管理")
@ApiSort(3)
@RestController
@RequestMapping("/admin/user")
public class AdminUsersController {
    @Value("${zfile.password.default}")
    private String DEFAULT_PASSWORD;

    @Resource
    private UsersService usersService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有添加的用户列表，按照用户名由小到大排序")
    @GetMapping("/list")
    public AjaxJson<List<User>> storageList() {
        List<User> list = usersService.selectAll();
        return AjaxJson.getSuccessData(list);
    }


    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "获取指定用户信息", notes = "获取指定用户信息")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户 id", required = true, dataTypeClass = Integer.class)
    @GetMapping("/{userId}/get")
    public AjaxJson<User> storageItem(@PathVariable Integer userId) {
        User user = usersService.findOneById(userId);
        return AjaxJson.getSuccessData(user);
    }


    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "保存用户参数", notes = "保存用户的所有参数")
    @PostMapping("/save")
    public AjaxJson<Integer> saveItem(@RequestBody User user) {
        User saved = usersService.save(user);
        Integer id = saved.getId();
        return AjaxJson.getSuccessData(id);
    }


    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "删除用户", notes = "删除用户基本设置和拓展设置")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户 id", required = true, dataTypeClass = Integer.class)
    @DeleteMapping("/{userId}/del")
    public AjaxJson<Void> deleteStorageItem(@PathVariable Integer userId) {
        User user = usersService.deleteById(userId);
        return AjaxJson.getSuccess();
    }

    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "校验用户 username 是否重复")
    @ApiImplicitParam(paramType = "query", name = "username", value = "username", required = true, dataTypeClass = String.class)
    @GetMapping("/exist")
    public AjaxJson<Boolean> existKey(String username) {
        boolean exist = usersService.existByUsername(username);
        return AjaxJson.getSuccessData(exist);
    }
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "userId", value = "用户 id", required = true, dataTypeClass = Integer.class),
        @ApiImplicitParam(paramType = "query", name = "password", value = "password", required = false, dataTypeClass = String.class)
    })
    @PostMapping("{userId}/resetPassword")
    public AjaxJson<Void> resetPassword(@PathVariable Integer userId, @RequestParam(value = "password", required = false)  String password) {
        if (userId == null) {
            throw new ServiceException(CodeMsg.BAD_REQUEST);
        }
        User user = usersService.findOneById(userId);
        if(password == null || password.length()<1){
            user.setPassword(DEFAULT_PASSWORD);
        }
        user.setPassword(SecureUtil.sha256(user.getPassword()));
        usersService.updatePasswdById(user);
        return AjaxJson.getSuccess();
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "启用用户", notes = "开启用户后可在前台显示")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户 id", required = true, dataTypeClass = Integer.class)
    @PostMapping("{userId}/enable")
    public AjaxJson<Void> enable(@PathVariable Integer userId) {
        User user = usersService.findOneById(userId);
        user.setEnabled(true);
        usersService.updateEnabledById(user);
        return AjaxJson.getSuccess();
    }


    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "停止用户", notes = "停用用户后不在前台显示")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户 id", required = true, dataTypeClass = Integer.class)
    @PostMapping("{userId}/disable")
    public AjaxJson<Void> disable(@PathVariable Integer userId) {
        User user = usersService.findOneById(userId);
        user.setEnabled(false);
        usersService.updateEnabledById(user);
        return AjaxJson.getSuccess();
    }



}