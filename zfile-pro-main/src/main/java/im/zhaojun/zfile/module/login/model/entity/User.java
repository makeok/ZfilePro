package im.zhaojun.zfile.module.login.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import im.zhaojun.zfile.module.login.model.enums.RoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 密码设置 entity
 *
 * @author zhaojun
 */
@Data
@ApiModel(value="密码设置")
@TableName(value = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 100L;

    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "ID, 新增无需填写", example = "1")
    private Integer id;


    @TableField(value = "username")
    @ApiModelProperty(value = "用户名", required = true, example = "zhangsan")
    private String username;


    @TableField(value = "username_zh")
    @ApiModelProperty(value = "用户中文名", required = true, example = "张三")
    private String usernameZh;


    @TableField(value = "password")
    @ApiModelProperty(value = "密码值", required = true, example = "123456")
    private String password;


    @TableField(value = "enabled")
    @ApiModelProperty(value = "是否启用", required = true, example = "true")
    private Boolean enabled;

    @TableField(value = "role")
    @ApiModelProperty(value = "角色", required = true, example = "普通用户")
    private String role = RoleEnum.NORMAL.getValue();

    @TableField(value = "remark")
    @ApiModelProperty(value = "描述", required = true, example = "描述")
    private String remark;

}