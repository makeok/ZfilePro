package im.zhaojun.zfile.webdav;

public enum ResultStatusCode {
    PERMISSION_UNAUTHORIZED(401, "未完成身份认证"),
    PERMISSION_FORBIDDEN(403, "无权限访问"),
    OK(200, "成功");
    //枚举中的构造方法必须是private修饰
    private int code;
    private String msg;

    //定义有参构造方法,枚举中的构造方法默认为private
    ResultStatusCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    public int getErrcode() {
        return code;
    }
    public String getErrmsg() {
        return msg;
    }

}
