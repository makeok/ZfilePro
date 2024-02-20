package im.zhaojun.zfile.module.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import im.zhaojun.zfile.module.login.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 存储源基本配置 Mapper 接口
 *
 * @author zhaojun
 */
@Mapper
public interface UsersMapper extends BaseMapper<User> {

    /**
     * 获取所有已启用的存储源, 并按照存储源排序值排序
     *
     * @return  存储源列表
     */
    User findOneById(@Param("id") Integer id);


    /**
     * 获取所有存储源, 并按照存储源排序值排序
     *
     * @return  存储源列表
     */
    List<User> selectAll();


    int selectEnabled();

    int updateFieldsById(User user);
    int updateZhById(User entity);
    int updatePasswdById(User user);
    int updateEnabledById(User user);

    int insertList(@Param("list") List<User> users);



}