package im.zhaojun.zfile.module.storage.model.request.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 复制文件请求参数
 *
 * @author zhaojun
 */
@Data
@ApiModel(description = "复制文件请求")
public class CopyFileRequest {

    @ApiModelProperty(value = "存储源 key", required = true, example = "local")
    @NotBlank(message = "存储源 key 不能为空")
    private String storageKey;

    @ApiModelProperty(value = "请求路径", example = "/", notes = "表示要复制的文件所在的文件夹")
    private String path;

    @ApiModelProperty(value = "文件名称", example = "a.txt", notes = "表示要复制的文件名称")
    private String name;

    @ApiModelProperty(value = "目标路径", example = "/", notes = "表示要复制到的文件夹")
    private String targetPath;

    @ApiModelProperty(value = "目标文件名称", example = "b.txt", notes = "表示要复制到的文件名称")
    private String targetName;

    @ApiModelProperty(value = "是否覆盖", example = "true", notes = "表示要覆盖文件")
    private Boolean overwrite;

    @ApiModelProperty(value = "源文件夹密码, 如果文件夹需要密码才能访问，则支持请求密码", example = "123456")
    private String password;

    @ApiModelProperty(value = "目标文件夹密码, 如果文件夹需要密码才能访问，则支持请求密码", example = "123456")
    private String targetPassword;

}