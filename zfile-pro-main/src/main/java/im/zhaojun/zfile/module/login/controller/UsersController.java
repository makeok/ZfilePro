package im.zhaojun.zfile.module.login.controller;

import cn.hutool.crypto.SecureUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import im.zhaojun.zfile.core.exception.ServiceException;
import im.zhaojun.zfile.core.util.AjaxJson;
import im.zhaojun.zfile.core.util.CodeMsg;
import im.zhaojun.zfile.module.login.model.entity.User;
import im.zhaojun.zfile.module.login.model.request.ResetPasswordRequest;
import im.zhaojun.zfile.module.login.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户基础设置模块接口
 *
 * @author zhaojun
 */
@Api(tags = "用户信息")
@ApiSort(3)
@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Resource
    private UsersService usersService;

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
        if (user == null || user.getId() == null) {
            throw new ServiceException(CodeMsg.BAD_REQUEST);
        }
        Integer id = usersService.saveUserInfo(user);
        return AjaxJson.getSuccessData(id);
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "userId", value = "用户 id", required = true, dataTypeClass = Integer.class),
        @ApiImplicitParam(paramType = "query", name = "password", value = "password", required = false, dataTypeClass = String.class)
    })
    @PostMapping("{userId}/resetPassword")
    public AjaxJson<Void> resetPassword(@PathVariable Integer userId, @RequestBody ResetPasswordRequest settingRequest) {
        if (userId == null || StringUtils.isEmpty(settingRequest.getPassword()) || StringUtils.isEmpty(settingRequest.getOldPassword())) {
            throw new ServiceException(CodeMsg.BAD_REQUEST);
        }
        User user = usersService.selectById(userId);
        //验证原密码
        if(!user.getPassword().equals(SecureUtil.sha256(settingRequest.getOldPassword()))){
            throw new ServiceException(CodeMsg.OLD_PASSWORD_FAULT);
        }
        user.setPassword(SecureUtil.sha256(settingRequest.getPassword()));
        usersService.updatePasswdById(user);
        return AjaxJson.getSuccess();
    }

}