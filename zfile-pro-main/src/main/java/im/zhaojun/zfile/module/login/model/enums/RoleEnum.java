package im.zhaojun.zfile.module.login.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 *
 * @author zhaojun
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    /**
     * 文件
     */
    NORMAL("普通用户"),

    /**
     * 文件夹
     */
    ADMIN("系统管理员");

    @EnumValue
    @JsonValue
    private final String value;

}