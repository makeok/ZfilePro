package im.zhaojun.zfile.core.config;

import cn.dev33.satoken.stp.StpInterface;
import im.zhaojun.zfile.module.login.model.entity.User;
import im.zhaojun.zfile.module.login.service.UsersService;
import im.zhaojun.zfile.module.storage.model.entity.StorageUser;
import im.zhaojun.zfile.module.storage.service.StorageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class SaRoleService implements StpInterface {
    @Autowired
    private UsersService usersService;
    @Autowired
    private StorageUserService storageUserService;
    //添加某些权限可以访问的
    @Override
    public List<String> getPermissionList(Object o, String s) {
//        Integer UserId= Integer.parseInt((String) o);
//        List<StorageUser> userList = storageUserService.findByUserId(UserId);
//        List<String> permissions = new ArrayList<>();
//        if(userList != null) {
//            for(StorageUser user : userList) {
//                permissions.add(user.getPermission());
//            }
//        }
//        return permissions;
        return  null;
    }

    //添加某些角色可以访问的
    @Override
    public List<String> getRoleList(Object o, String lognType) {
        //o属性就是刚刚绑定的id，通过这个id去数据库查询权限
        Integer id= Integer.parseInt((String) o);
        User user = usersService.findOneById(id);
        List<String> list = new ArrayList<String>();
        //“*”权限表示什么都可以访问
        list.add(user.getRole());
        return list;
    }
}
