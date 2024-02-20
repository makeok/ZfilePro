package im.zhaojun.zfile.module.login.model.result;

import im.zhaojun.zfile.module.login.model.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "登录返回类")
public class LoginResult {
    User user;
    String token;
}
