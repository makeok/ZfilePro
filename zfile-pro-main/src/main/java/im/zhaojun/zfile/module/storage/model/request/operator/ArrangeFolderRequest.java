package im.zhaojun.zfile.module.storage.model.request.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 新建文件夹请求参数
 *
 * @author zhaojun
 */
@Data
@ApiModel(description = "新建文件夹请求类")
public class ArrangeFolderRequest {

    @ApiModelProperty(value = "存储源 key", required = true, example = "local")
    @NotBlank(message = "存储源 key 不能为空")
    private String storageKey;

    @ApiModelProperty(value = "请求路径", example = "/", notes = "表示在哪个文件夹下创建文件夹")
    private String path = "/";

    @ApiModelProperty(value = "格式", example = "yyyy/MM/dd", notes = "文件夹的格式化名称，如：yyyy/MM/dd\"")
    private String format;
    
    @ApiModelProperty(value = "文件夹密码, 如果文件夹需要密码才能访问，则支持请求密码", example = "123456")
    private String password;

}