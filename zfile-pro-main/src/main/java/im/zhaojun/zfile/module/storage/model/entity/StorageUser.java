package im.zhaojun.zfile.module.storage.model.entity;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import im.zhaojun.zfile.module.storage.model.enums.SearchModeEnum;
import im.zhaojun.zfile.module.storage.model.enums.StorageTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 存储源基本属性 entity
 *
 * @author zhaojun
 */
@Data
@AllArgsConstructor

@ApiModel(description = "存储源授权用户属性")
@TableName(value = "storage_user")
public class StorageUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID, 新增无需填写", example = "1")
    private Integer id;


    @TableField(value = "`user_id`")
    @ApiModelProperty(value = "用户id", example = "1")
    private Integer userId;


    @TableField(value = "`storage_id`")
    @ApiModelProperty(value = "数据源id", example = "1")
    private Integer storageId;

    @TableField(value = "`permission`")
    @ApiModelProperty(value = "权限", example = "true", notes = "读、写")
    private String permission;

}