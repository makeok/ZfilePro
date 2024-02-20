package im.zhaojun.zfile.module.link.controller;

import cn.hutool.core.util.BooleanUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import im.zhaojun.zfile.core.exception.IllegalDownloadLinkException;
import im.zhaojun.zfile.core.util.AjaxJson;
import im.zhaojun.zfile.core.util.StringUtils;
import im.zhaojun.zfile.module.config.model.dto.SystemConfigDTO;
import im.zhaojun.zfile.module.config.service.SystemConfigService;
import im.zhaojun.zfile.module.link.model.entity.ShortLink;
import im.zhaojun.zfile.module.link.model.request.BatchGenerateLinkRequest;
import im.zhaojun.zfile.module.link.model.result.BatchGenerateLinkResponse;
import im.zhaojun.zfile.module.link.service.LinkDownloadService;
import im.zhaojun.zfile.module.link.service.ShortLinkService;
import im.zhaojun.zfile.module.storage.service.StorageSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 短链接口
 *
 * @author zhaojun
 */
@Api(tags = "直短链模块")
@ApiSort(5)
@Controller
@Slf4j
public class ShortLinkController {

    @Resource
    private SystemConfigService systemConfigService;

    @Resource
    private ShortLinkService shortLinkService;

    @Resource
    private StorageSourceService storageSourceService;

    @Resource
    private LinkDownloadService linkDownloadService;

    @PostMapping("/api/short-link/batch/generate")
    @ResponseBody
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "生成短链", notes = "对指定存储源的某文件路径生成短链")
    public AjaxJson<List<BatchGenerateLinkResponse>> generatorShortLink(@RequestBody @Valid BatchGenerateLinkRequest batchGenerateLinkRequest) {
        List<BatchGenerateLinkResponse> result = new ArrayList<>();

        // 获取站点域名
        SystemConfigDTO systemConfig = systemConfigService.getSystemConfig();

        // 是否允许使用短链和短链，如果都不允许，则提示禁止生成.
        Boolean showShortLink = systemConfig.getShowShortLink();
        Boolean showPathLink = systemConfig.getShowPathLink();
        if ( BooleanUtil.isFalse(showShortLink) && BooleanUtil.isFalse(showPathLink)) {
            throw new IllegalDownloadLinkException("当前系统不允许使用直链和短链.");
        }

        String domain = systemConfig.getDomain();
        Long expireTime = batchGenerateLinkRequest.getExpireTime();
        String storageKey = batchGenerateLinkRequest.getStorageKey();
        Integer storageId = storageSourceService.findIdByKey(storageKey);

        for (String path : batchGenerateLinkRequest.getPaths()) {
            // 拼接全路径地址.
            String fullPath = StringUtils.concat(path);
            ShortLink shortLink = shortLinkService.generatorShortLink(storageId, fullPath, expireTime);
            String shortUrl = StringUtils.removeDuplicateSlashes(domain + "/s/" + shortLink.getShortKey());
            String pathLink = StringUtils.generatorPathLink(storageKey, fullPath);
            result.add(new BatchGenerateLinkResponse(shortUrl, pathLink));
        }
        return AjaxJson.getSuccessData(result);
    }


    @GetMapping("/s/{key}")
    @ResponseStatus(HttpStatus.FOUND)
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "跳转短链", notes = "根据短链 key 跳转（302 重定向）到对应的直链.")
    @ApiImplicitParam(paramType = "path", name = "key", value = "短链 key", required = true, dataTypeClass = String.class)
    public void parseShortKey(@PathVariable String key) throws IOException {
        linkDownloadService.handlerShortLink(key);
    }

}