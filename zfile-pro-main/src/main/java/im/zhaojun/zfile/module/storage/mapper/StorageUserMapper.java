package im.zhaojun.zfile.module.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import im.zhaojun.zfile.module.storage.model.entity.StorageUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 存储源拓展设置 Mapper 接口
 *
 * @author zhaojun
 */
@Mapper
public interface StorageUserMapper extends BaseMapper<StorageUser> {

    List<StorageUser> findByStorageId(@Param("storageId") Integer storageId);
    List<StorageUser> findByUserId(@Param("userId") Integer userId);
    int deleteByStorageId(@Param("storageId") Integer storageId);
    int deleteByUserId(@Param("userId") Integer userId);


}