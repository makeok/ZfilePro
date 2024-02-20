package im.zhaojun.zfile.module.storage.service;

import im.zhaojun.zfile.module.storage.mapper.StorageUserMapper;
import im.zhaojun.zfile.module.storage.model.entity.StorageUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 存储源拓展配置 Service
 *
 * @author zhaojun
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "storageUserService")
public class StorageUserService {

    @Resource
    private StorageUserMapper storageUserMapper;

    @Cacheable(key = "'findByStorageId' + #storageId")
    public List<StorageUser> findByStorageId(Integer storageId) {
        return storageUserMapper.findByStorageId(storageId);
    }
    @Cacheable(key = "'findByUserId' + #userId")
    public List<StorageUser> findByUserId(Integer userId) {
        return storageUserMapper.findByUserId(userId);
    }

    @CacheEvict(key = "'findByStorageId' + #storageId")
    public int deleteByStorageId(Integer storageId) {
        int deleteSize = storageUserMapper.deleteByStorageId(storageId);
        log.info("删除存储源ID为[{}]的授权配置 {} 条", storageId, deleteSize);
        return deleteSize;
    }

    @CacheEvict(key = "'findByUserId' + #userId")
    public int deleteByUserId(Integer userId) {
        int deleteSize =  storageUserMapper.deleteByUserId(userId);
        log.info("删除用户ID为[{}]的授权配置 {} 条", userId, deleteSize);
        return deleteSize;
    }
    @Caching(evict = {
            @CacheEvict(key = "'findByStorageId' + #storageId"),
            @CacheEvict(key = "'findByUserId' + #userId")
    })
    public int deleteBystorageIdAndUserId(Integer storageId, Integer userId) {
        StorageUser storageUser =  findByStorageIdAndUserId(storageId, userId);
        int deleteSize = storageUserMapper.deleteById(storageUser.getId());
        log.info("删除 存储源ID为[{}],用户ID为[{}]的授权配置 {} 条", storageId, userId, deleteSize);
        return deleteSize;
    }

    @Cacheable(key = "'findByStorageIdAndUserId' + #storageId + '-'+ #userId")
    public StorageUser findByStorageIdAndUserId(Integer storageId, Integer userId) {
        return storageUserMapper
                .findByStorageId(storageId)
                .stream()
                .filter(storageUser -> userId.equals(storageUser.getUserId()))
                .findFirst()
                .orElse(null);
    }


    /**
     * 批量保存
     *
     * @param   storageId
     *          存储源 ID
     *
     * @param   configList
     *          实体对象集合
     */
    @CacheEvict(allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Integer storageId, Collection<StorageUser> configList) {
        deleteByStorageId(storageId);
        log.info("更新存储源 ID 为 {} 的授权配置 {} 条", storageId, configList.size());
        configList.forEach(storageUser -> {
            storageUser.setStorageId(storageId);
            storageUser.setId(null);
            storageUserMapper.insert(storageUser);
    
            if (log.isDebugEnabled()) {
                log.debug("新增存储源参数配置, 存储源ID: {}, 用户ID: {}, 权限: {}",
                        storageUser.getStorageId(), storageUser.getUserId(),
                        storageUser.getPermission());
            }
        });
    }

    @CacheEvict(allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(Integer storageId, List<StorageUser> storageUserList) {
        storageUserList.forEach(storageUser -> {
            storageUser.setStorageId(storageId);
            storageUserMapper.updateById(storageUser);
            if (log.isDebugEnabled()) {
                log.debug("更新存储源参数配置, 存储源ID: {}, 用户ID: {}, 权限: {}",
                        storageUser.getStorageId(), storageUser.getUserId(),
                        storageUser.getPermission());
            }
        });
    }

}