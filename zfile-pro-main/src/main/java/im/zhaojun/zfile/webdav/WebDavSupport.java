package im.zhaojun.zfile.webdav;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import im.zhaojun.zfile.module.login.model.entity.User;
import im.zhaojun.zfile.module.login.model.enums.PermissionEnum;
import im.zhaojun.zfile.module.login.service.UsersService;
import im.zhaojun.zfile.module.storage.model.entity.StorageUser;
import im.zhaojun.zfile.module.storage.service.StorageSourceService;
import im.zhaojun.zfile.module.storage.service.StorageUserService;
import org.apache.catalina.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * @Author hex.wang
 * @Class WebDavSupport
 * @Description
 * @Date 2022/1/5 14:45
 */
@WebServlet(name = "MydavServlet", urlPatterns = {"/webdav/*"},loadOnStartup=2
        , asyncSupported = true
        , initParams = {@WebInitParam(name = "listings", value = "true"),
        @WebInitParam(name = "readonly", value = "false"),
        @WebInitParam(name = "debug", value = "0")
})

public class WebDavSupport extends WebdavServlet {
    @Autowired
    private WebdavHandler webdavHandler;
    private static final HashSet<String> WRITE_METHODS = new HashSet<String>(
            Arrays.asList("PROPPATCH","PUT","POST","LOCK","UNLOCK","MOVE","COPY","DELETE","MKCOL") );
    @Autowired
    private UsersService usersService;
    @Autowired
    private StorageSourceService storageSourceService;
    @Autowired
    private StorageUserService storageUserService;

    @Override
    public void init() throws ServletException {
        WebResourceRoot webResourceRoot = (WebResourceRoot) getServletContext().getAttribute(Globals.RESOURCES_ATTR);
//        StandardRoot root = new StandardRoot(webResourceRoot.getContext());
//        getServletContext().setAttribute(Globals.RESOURCES_ATTR, root);
        webdavHandler.setWebDavSupport(this);
        webdavHandler.setWebResourceRoot(webResourceRoot);
        webdavHandler.initDavPath();
        super.init();
    }

    public ResultStatusCode auth(ServletRequest req, ServletResponse res) {
        HttpServletRequest request = (HttpServletRequest) req;
        final String method = request.getMethod();
        final String url = request.getRequestURI();// getRequestURI()=/webdav/photo/a.jpg
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.length() > 6) {
            String headStr = authorization.substring(0, 5).toLowerCase();
            if (headStr.compareTo("basic") == 0) {
                String base64 = authorization.replaceFirst("Basic\\s+", "");
                String string = new String(Base64.decodeBase64(base64), StandardCharsets.UTF_8);
                String[] array = string.split(":");
                if (array.length == 2 && StringUtils.isNoneEmpty(array[0], array[1])) {
                    String username = array[0];
                    String password = array[1];
                    User dbUser = usersService.findOneByUsername(username);
                    // 验证密码
                    if (dbUser !=null && Objects.equals(dbUser.getPassword(), SecureUtil.sha256(password))) {
                        String path = url.replace("/webdav","");// getServletPath()=/photo/a.jpg
                        if(StringUtils.isEmpty(path) || "/".equals(path)) {//webdav根目录，无storageKey
                            return ResultStatusCode.OK;
                        }
                        String storageKey = path.substring(1, path.indexOf('/', 1)<0?path.length():path.indexOf('/', 1));
                        // 验证权限
                        Integer storageId = storageSourceService.findIdByKey(storageKey);
                        if (storageId != null) {
                            // 获取用户的授权存储源ID
                            StorageUser storageUser = storageUserService.findByStorageIdAndUserId(storageId, dbUser.getId());
                            if(storageUser != null) {
                                if(WRITE_METHODS.contains(method)) {// 可写
                                    if(storageUser.getPermission().equals(PermissionEnum.WRITE.getValue())){
                                        return ResultStatusCode.OK;
                                    }
                                }else {// 可读
                                    return ResultStatusCode.OK;
                                }
                            }
                        }
                        return ResultStatusCode.PERMISSION_FORBIDDEN;
                    }
                }
            }
        }
        return ResultStatusCode.PERMISSION_UNAUTHORIZED;
    }


    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        ResultStatusCode result = auth(req, res);
        if(super.debug > 0){
            HttpServletRequest request = (HttpServletRequest) req;
            final String method = request.getMethod();
            final String url = request.getRequestURI();// getRequestURI()=/webdav/photo/a.jpg
            this.log("WebDavSupport.service: "+"[" + method + "] " + url+",result.code=" + result.getErrcode() + ", result.msg=" + result.getErrmsg());
        }
        if (result.equals(ResultStatusCode.OK)) {
            super.service(req, res);
        }else{
            if(result.equals(ResultStatusCode.PERMISSION_UNAUTHORIZED)) {
                HttpServletResponse httpResponse = (HttpServletResponse) res;
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json; charset=utf-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
                ObjectMapper mapper = new ObjectMapper();

                ResultMsg resultMsg = new ResultMsg(ResultStatusCode.PERMISSION_UNAUTHORIZED.getErrcode(),
                        ResultStatusCode.PERMISSION_UNAUTHORIZED.getErrmsg(), null);
                httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
                return;
            }else{
                HttpServletResponse httpResponse = (HttpServletResponse) res;
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json; charset=utf-8");
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                ObjectMapper mapper = new ObjectMapper();
                ResultMsg resultMsg = new ResultMsg(ResultStatusCode.PERMISSION_FORBIDDEN.getErrcode(),
                        ResultStatusCode.PERMISSION_FORBIDDEN.getErrmsg(), null);
                httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
                return;
            }
        }
    }

}
