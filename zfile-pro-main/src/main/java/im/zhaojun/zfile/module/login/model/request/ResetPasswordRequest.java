package im.zhaojun.zfile.module.login.model.request;import io.swagger.annotations.ApiModel;import io.swagger.annotations.ApiModelProperty;import lombok.Data;import javax.validation.constraints.NotBlank;/** * 用户修改密码请求参数类 * * @author zhaojun */@Data@ApiModel(description = "用户修改密码请求参数类")public class ResetPasswordRequest {    @ApiModelProperty(value = "原密码", required = true, example = "123456")    @NotBlank(message = "原密码不能为空")    private String oldPassword;    @ApiModelProperty(value = "密码", required = true, example = "123456")    @NotBlank(message = "密码不能为空")    private String password;}