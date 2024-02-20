package im.zhaojun.zfile.module.storage.controller.base;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import im.zhaojun.zfile.core.util.AjaxJson;
import im.zhaojun.zfile.module.storage.model.entity.StorageUser;
import im.zhaojun.zfile.module.storage.service.StorageUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 存储源密码维护接口
 *
 * @author zhaojun
 */
@Api(tags = "存储源模块-密码文件夹")
@ApiSort(6)
@RestController
@RequestMapping("/admin")
public class StorageUserController {

    @Resource
    private StorageUserService storageUserService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取存储源授权列表", notes = "根据存储源 ID 获取存储源授权列表")
    @ApiImplicitParam(paramType = "path", name = "storageId", value = "存储源 id", required = true, dataTypeClass = Integer.class)
    @GetMapping("/storage/{storageId}/auth")
    public AjaxJson<List<StorageUser>> getUserList(@PathVariable Integer storageId) {
        return AjaxJson.getSuccessData(storageUserService.findByStorageId(storageId));
    }


    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "保存存储源授权列表", notes = "保存指定存储源 ID 设置的授权列表")
    @ApiImplicitParam(paramType = "path", name = "storageId", value = "存储源 id", required = true, dataTypeClass = Integer.class)
    @PostMapping("/storage/{storageId}/auth")
    public AjaxJson<Void> savePasswordList(@PathVariable Integer storageId, @RequestBody List<StorageUser> list) {
        storageUserService.saveBatch(storageId, list);
        return AjaxJson.getSuccess();
    }

}