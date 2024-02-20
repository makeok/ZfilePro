package im.zhaojun.zfile.module.storage.model.param;

import im.zhaojun.zfile.module.storage.annotation.StorageParamItem;
import im.zhaojun.zfile.module.storage.model.enums.StorageParamTypeEnum;
import lombok.Getter;

/**
 * 本地存储初始化参数
 *
 * @author zhaojun
 */
@Getter
public class LocalParam extends ProxyDownloadParam {

	@StorageParamItem(name = "文件路径", description = "只支持绝对路径<br>Docker 方式部署的话需提前映射宿主机路径！ " +
			"(<a class='link' target='_blank' href='https://docs.docker.com/engine/reference/run/#volume-shared-filesystems'>配置文档</a>)")
	private String filePath;

	@StorageParamItem(name = "是否启用webdav", type = StorageParamTypeEnum.SWITCH, defaultValue = "false", description = "启用后通过/webdav/storageKey进行webdav相关操作！")
	private Boolean enableWebdav = false;

	@StorageParamItem(name = "是否启用缩略图", type = StorageParamTypeEnum.SWITCH, defaultValue = "false", description = "启用后画廊模式默认展示缩略图，查看全图时为原图！")
	private Boolean enableThumbnail = false;
}