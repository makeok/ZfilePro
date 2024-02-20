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
public enum PermissionEnum {

    /**
     * 文件
     */
    READ("读"),

    /**
     * 文件夹
     */
    WRITE("写");

    @EnumValue
    @JsonValue
    private final String value;

}