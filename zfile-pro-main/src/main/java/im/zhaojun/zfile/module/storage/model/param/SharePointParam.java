package im.zhaojun.zfile.module.storage.model.param;

import im.zhaojun.zfile.module.storage.annotation.StorageParamItem;
import lombok.Getter;

/**
 * SharePoint 初始化参数
 *
 * @author zhaojun
 */
@Getter
public class SharePointParam extends MicrosoftDriveParam {

	@StorageParamItem(name = "clientId", defaultValue = "${zfile.onedrive.clientId}", order = 1)
	private String clientId;

	@StorageParamItem(name = "SecretKey", defaultValue = "${zfile.onedrive.clientSecret}", order = 2)
	private String clientSecret;

	@StorageParamItem(name = "网站", order = 5, description = "如此处选择不到网站，请检查世纪互联中网站隐私设置是否为\"公用-组织中的任何人都可访问此站点\"")
	private String siteId;

	@StorageParamItem(name = "子目录", order = 6, description = "表示 SharePoint 子列表/子网站，在世纪互联网站 Tab 卡中 \"网站内容\" 新增.")
	private String listId;

}