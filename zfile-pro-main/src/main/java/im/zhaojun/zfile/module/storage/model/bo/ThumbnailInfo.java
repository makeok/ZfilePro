
package im.zhaojun.zfile.module.storage.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

/**
 * 又拍云上传认证信息 model
 *
 * @author zhaojun
 */
@Data
@AllArgsConstructor
public class ThumbnailInfo {

    private File src;

    private File dst;

}