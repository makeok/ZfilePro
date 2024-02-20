package im.zhaojun.zfile.module.storage.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import im.zhaojun.zfile.core.exception.StorageSourceException;
import im.zhaojun.zfile.core.exception.file.InvalidStorageSourceException;
import im.zhaojun.zfile.core.util.CodeMsg;
import im.zhaojun.zfile.module.filter.model.entity.FilterConfig;
import im.zhaojun.zfile.module.filter.service.FilterConfigService;
import im.zhaojun.zfile.module.link.service.ShortLinkService;
import im.zhaojun.zfile.module.log.service.DownloadLogService;
import im.zhaojun.zfile.module.password.model.dto.VerifyResultDTO;
import im.zhaojun.zfile.module.password.model.entity.PasswordConfig;
import im.zhaojun.zfile.module.password.service.PasswordConfigService;
import im.zhaojun.zfile.module.readme.model.entity.ReadmeConfig;
import im.zhaojun.zfile.module.readme.service.ReadmeConfigService;
import im.zhaojun.zfile.module.storage.context.StorageSourceContext;
import im.zhaojun.zfile.module.storage.convert.StorageSourceConvert;
import im.zhaojun.zfile.module.storage.mapper.StorageSourceMapper;
import im.zhaojun.zfile.module.storage.model.dto.StorageSourceAllParamDTO;
import im.zhaojun.zfile.module.storage.model.dto.StorageSourceDTO;
import im.zhaojun.zfile.module.storage.model.entity.StorageSource;
import im.zhaojun.zfile.module.storage.model.entity.StorageSourceConfig;
import im.zhaojun.zfile.module.storage.model.enums.StorageTypeEnum;
import im.zhaojun.zfile.module.storage.model.request.admin.CopyStorageSourceRequest;
import im.zhaojun.zfile.module.storage.model.request.admin.UpdateStorageSortRequest;
import im.zhaojun.zfile.module.storage.model.request.base.FileListConfigRequest;
import im.zhaojun.zfile.module.storage.model.request.base.SaveStorageSourceRequest;
import im.zhaojun.zfile.module.storage.model.result.StorageSourceConfigResult;
import im.zhaojun.zfile.webdav.WebdavHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 存储源基本信息 Service
 *
 * @author zhaojun
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "storageSource")
public class StorageSourceService {

    @Resource
    private StorageSourceMapper storageSourceMapper;
    
    @Resource
    private StorageSourceService storageSourceService;
    
    @Resource
    private StorageSourceContext storageSourceContext;
    
    @Resource
    private StorageSourceConvert storageSourceConvert;
    
    @Resource
    private StorageSourceConfigService storageSourceConfigService;
    
    @Resource
    private FilterConfigService filterConfigService;
    
    @Resource
    private PasswordConfigService passwordConfigService;
    
    @Resource
    private ReadmeConfigService readmeConfigService;
    
    @Resource
    private ShortLinkService shortLinkService;
    
    @Resource
    private DownloadLogService downloadLogService;

    @Resource
    private WebdavHandler webdavHandler;//更新webdav
    /**
     * 获取所有存储源列表
     *
     * @return 存储源列表
     */
    public List<StorageSource> findAllOrderByOrderNum() {
        return storageSourceMapper.findAllOrderByOrderNum();
    }


    /**
     * 获取所有已启用的存储源列表，按照存储源的排序号排序
     *
     * @return 已启用的存储源列表
     */
    public List<StorageSource> findAllEnableOrderByOrderNum() {
        return storageSourceMapper.findListByEnableOrderByOrderNum();
    }
    
    
    /**
     * 获取指定存储源设置
     *
     * @param   id
     *          存储源 ID
     *
     * @return  存储源设置
     */
    @Cacheable(key = "#id", unless = "#result == null")
    public StorageSource findById(Integer id) {
        return storageSourceMapper.selectById(id);
    }
    
    
    /**
     * 根据存储源 key 获取存储源
     *
     * @param   storageKey
     *          存储源 key
     *
     * @throws  InvalidStorageSourceException   存储源不存在时, 抛出异常.
     *
     * @return  存储源信息
     */
    @Cacheable(key = "#storageKey", unless = "#result == null")
    public StorageSource findByStorageKey(String storageKey) {
        return storageSourceMapper.findByStorageKey(storageKey);
    }
    
    
    /**
     * 根据存储源 key 清除 key 的缓存
     *
     * @param   storageKey
     *          存储源 key
     */
    @CacheEvict(key = "#storageKey")
    public void clearCacheByStorageKey(String storageKey) {}


    /**
     * 根据存储源 key 获取存储源 id
     *
     * @param   storageKey
     *          存储源 key
     *
     * @return  存储源信息
     */
    public Integer findIdByKey(String storageKey) {
        return Optional.ofNullable(storageSourceService.findByStorageKey(storageKey)).map(StorageSource::getId).orElse(null);
    }
    
    
    /**
     * 根据存储源 id 获取存储源 key
     *
     * @param   id
     *          存储源 id
     *
     * @return  存储源 key
     */
    public String findStorageKeyById(Integer id){
        return Optional.ofNullable(storageSourceService.findById(id)).map(StorageSource::getKey).orElse(null);
    }
    
    
    /**
     * 根据 id 获取指定存储源的类型.
     *
     * @param   id
     *          存储源 ID
     *
     * @return  存储源对应的类型.
     */
    public StorageTypeEnum findStorageTypeById(Integer id) {
        return Optional.ofNullable(storageSourceService.findById(id)).map(StorageSource::getType).orElse(null);
    }
    
    
    /**
     * 获取指定存储源 DTO 对象, 此对象包含详细的参数设置.
     *
     * @param   id
     *          存储源 ID
     *
     * @return  存储源 DTO
     */
    @Cacheable(key = "'dto-' + #id", unless = "#result == null")
    public StorageSourceDTO findDTOById(Integer id) {
        // 将参数列表通过反射写入到 StorageSourceAllParam 中.
        StorageSourceAllParamDTO storageSourceAllParam = new StorageSourceAllParamDTO();
        storageSourceConfigService.selectStorageConfigByStorageId(id)
                .forEach(storageSourceConfig ->
                        ReflectUtil.setFieldValue(storageSourceAllParam, storageSourceConfig.getName(), storageSourceConfig.getValue())
                );
        
        // 获取数据库对象，转为 dto 对象返回
        StorageSource storageSource = findById(id);
        return storageSourceConvert.entityToDTO(storageSource, storageSourceAllParam);
    }
    
    
    /**
     * 判断存储源 key 是否已存在 (不读取缓存)
     *
     * @param   storageKey
     *          存储源 key
     *
     * @return  是否已存在
     */
    public boolean existByStorageKey(String storageKey) {
        return storageSourceService.findByStorageKey(storageKey) != null;
    }


    /**
     * 删除指定存储源设置, 会级联删除其参数设置
     *
     * @param   id
     *          存储源 ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(key = "'dto-' + #id"),
            @CacheEvict(key = "#result.key", condition = "#result != null")
    })
    public StorageSource deleteById(Integer id) {
        log.info("删除 id 为 {} 的存储源", id);
        StorageSource storageSource = findById(id);
        
        if (storageSource == null) {
            String msg = StrUtil.format("删除存储源时检测到 id 为 {} 的存储源不存在", id);
            throw new StorageSourceException(CodeMsg.STORAGE_SOURCE_NOT_FOUND, id, msg);
        }
        
        String storageKey = storageSource.getKey();
    
        int deleteEntitySize = storageSourceMapper.deleteById(id);
        int deleteConfigSize = storageSourceConfigService.deleteByStorageId(id);
    
        int clearPasswordSize = passwordConfigService.deleteByStorageId(id);
        int clearFilterSize = filterConfigService.deleteByStorageId(id);
        int clearReadmeSize = readmeConfigService.deleteByStorageId(id);
    
        int clearShortLinkSize = shortLinkService.deleteByStorageId(id);
        int clearDownloadLogSize = downloadLogService.deleteByStorageKey(storageKey);
    
        storageSourceContext.destroy(id);
        log.info("尝试删除存储源 {} 成功, 已清理相关数据及上下文环境（存储源 {} 条、存储源设置 {} 条、" +
                "密码规则 {} 条、过滤规则 {} 条，目录文档 {} 条、直链 {} 条、直链下载日志 {} 条）",
                id, deleteEntitySize, deleteConfigSize,
                clearPasswordSize, clearFilterSize, clearReadmeSize, clearShortLinkSize, clearDownloadLogSize);

        this.checkDav(storageSource);
        return storageSource;
    }

    
    /**
     * 交换存储源排序
     *
     * @param   updateStorageSortRequestList
     *          更新排序的存储源 id 及排序值列表
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public void updateStorageSort(List<UpdateStorageSortRequest> updateStorageSortRequestList) {
        for (int i = 0; i < updateStorageSortRequestList.size(); i++) {
            UpdateStorageSortRequest item = updateStorageSortRequestList.get(i);
            if (!Objects.equals(i, item.getOrderNum())) {
                log.info("变更存储源 {} 顺序号为 {}", item.getId(), i);
                storageSourceMapper.updateSetOrderNumById(i, item.getId());
            }
        }
    }
    
    
    @Caching(evict = {
            @CacheEvict(key = "#entity.id"),
            @CacheEvict(key = "#entity.key"),
            @CacheEvict(key = "'dto-' + #entity.id")
    })
    public void updateById(StorageSource entity) {
        storageSourceMapper.updateById(entity);
    }
    
    
    /**
     * 保存存储源基本信息及其对应的参数设置
     *
     * @param   saveStorageSourceRequest
     *          存储源 DTO 对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer saveStorageSource(SaveStorageSourceRequest saveStorageSourceRequest) {
        log.info("尝试保存存储源, id: {}, name: {}, key: {}, type: {}",
                saveStorageSourceRequest.getId(), saveStorageSourceRequest.getName(),
                saveStorageSourceRequest.getKey(), saveStorageSourceRequest.getType().getDescription());

        // 转换为存储源 entity 对象
        StorageSource storageSource = storageSourceConvert.saveRequestToEntity(saveStorageSourceRequest);
        StorageSourceDTO old = null;
        if (storageSource.getId() != null) {
            old = storageSourceService.findDTOById(storageSource.getId());
        }
        // 保存或更新存储源
        StorageSource dbSaveResult = storageSourceService.saveOrUpdate(storageSource);
    
        log.info("保存存储源成功, id: {}, name: {}, key: {}, type: {}",
                dbSaveResult.getId(), dbSaveResult.getName(),
                dbSaveResult.getKey(), dbSaveResult.getType().getDescription());
        
        // 存储源 ID
        Integer storageId = dbSaveResult.getId();
        
        // 保存存储源参数
        List<StorageSourceConfig> storageSourceConfigList =
                storageSourceConfigService.toStorageSourceConfigList(storageId,
                                                                    dbSaveResult.getType(),
                                                                    saveStorageSourceRequest.getStorageSourceAllParam());
        storageSourceConfigService.saveBatch(storageId, storageSourceConfigList);
        log.info("保存存储源参数成功，尝试根据参数初始化存储源, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());


        // 初始化并检查是否可用
        storageSourceContext.init(storageSource);
        log.info("根据参数初始化存储源成功, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());

        this.checkDav(storageSource, saveStorageSourceRequest, old);

        return storageId;
    }
    
    
    /**
     * 保存或修改存储源设置，如果没有 id 则新增，有则更新，且会检测是否填写 key，如果没写，则自动将 id 设置为 key 并保存。
     *
     * @param   storageSource
     *          存储源对象
     *
     * @return  保存后对象
     */
    @Caching(evict = {
            @CacheEvict(key = "#result.id"),
            @CacheEvict(key = "'dto-' + #result.id"),
            @CacheEvict(key = "#result.key")
    })
    public StorageSource saveOrUpdate(StorageSource storageSource) {
        // 保存存储源基本信息
        if (storageSource.getId() == null) {
            storageSourceMapper.insert(storageSource);
        } else {
            // 判断是否修改了存储源别名，如果修改了则清除之前存储源别名的缓存。
            StorageSource originStorageSource = storageSourceMapper.selectById(storageSource.getId());
            if (!StrUtil.equals(originStorageSource.getKey(), storageSource.getKey())) {
                storageSourceService.clearCacheByStorageKey(originStorageSource.getKey());
            }
            storageSourceMapper.updateById(storageSource);
        }
        
        // 如果没输入存储源 key, 则自动将 id 设置为 key
        if (StrUtil.isEmpty(storageSource.getKey()) && !StrUtil.equals(storageSource.getId().toString(), storageSource.getKey())) {
            storageSource.setKey(Convert.toStr(storageSource.getId()));
            storageSourceMapper.updateById(storageSource);
        }
        return storageSource;
    }
    
    
    public StorageSourceConfigResult getStorageConfigSource(FileListConfigRequest fileListConfigRequest) {
        String storageKey = fileListConfigRequest.getStorageKey();
        String path = fileListConfigRequest.getPath();
        
        // 判断存储源是否存在.
        StorageSource storageSource = storageSourceService.findByStorageKey(storageKey);
        if (storageSource == null) {
            throw new InvalidStorageSourceException("通过存储源 key 未找到存储源, key: " + storageKey);
        }
        
        // 根据存储源 key 获取存储源 id
        Integer storageId = storageSource.getId();
    
        VerifyResultDTO verifyPassword = passwordConfigService.verifyPassword(storageId, path, fileListConfigRequest.getPassword());
    
        ReadmeConfig readmeByPath = null;
        if (verifyPassword.isPassed()) {
            // 获取指定存储源路径下的 readme 信息
            readmeByPath = readmeConfigService.getByStorageAndPath(storageId, path, storageSource.getCompatibilityReadme());
        } else {
            log.info("文件夹密码验证失败，不获取 readme 信息, storageId: {}, path: {}, password: {}", storageId, path, fileListConfigRequest.getPassword());
        }
    
        // 获取指定存储源路径下的 readme 信息
        return storageSourceConvert.entityToConfigResult(storageSource, readmeByPath);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer copy(CopyStorageSourceRequest copyStorageSourceRequest) {
        String toKey = copyStorageSourceRequest.getToKey();
        boolean existByStorageKey = storageSourceService.existByStorageKey(toKey);
        if (existByStorageKey) {
            throw new RuntimeException("存储源别名已存在: " + toKey);
        }
        Integer fromStorageId = copyStorageSourceRequest.getFromId();
        // 复制存储源
        StorageSource storageSource = storageSourceService.findById(fromStorageId);
        if (storageSource == null) {
            throw new InvalidStorageSourceException("通过存储源 id 未找到存储源, id: " + fromStorageId);
        }
        StorageSource newStorageSource = new StorageSource();
        BeanUtils.copyProperties(storageSource, newStorageSource);
        newStorageSource.setId(null);
        newStorageSource.setKey(copyStorageSourceRequest.getToKey());
        newStorageSource.setName(copyStorageSourceRequest.getToName());
        StorageSource dbSaveResult = storageSourceService.saveOrUpdate(newStorageSource);
        log.info("复制存储源成功，复制后 id: {}, name: {}, key: {}, type: {}",
                dbSaveResult.getId(), dbSaveResult.getName(),
                dbSaveResult.getKey(), dbSaveResult.getType().getDescription());

        // 复制存储源基础参数
        Integer newStorageId = dbSaveResult.getId();
        List<StorageSourceConfig> storageSourceConfigList = storageSourceConfigService.selectStorageConfigByStorageId(fromStorageId);
        storageSourceConfigList.forEach(storageSourceConfig -> {
            storageSourceConfig.setId(null);
            storageSourceConfig.setStorageId(newStorageId);
        });
        storageSourceConfigService.saveBatch(newStorageId, storageSourceConfigList);
        log.info("保存复制后的存储源参数成功，尝试根据参数初始化存储源, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());

        // 初始化存储源
        storageSourceContext.init(dbSaveResult);
        log.info("初始化存储源成功, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());

        // 复制存储源过滤配置
        List<FilterConfig> filterConfigList = filterConfigService.findByStorageId(fromStorageId);
        filterConfigList.forEach(filterConfig -> {
            filterConfig.setId(null);
            filterConfig.setStorageId(newStorageId);
        });
        filterConfigService.batchSave(newStorageId, filterConfigList);
        log.info("复制存储源过滤配置成功, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());

        // 复制存储源密码配置
        List<PasswordConfig> passwordConfigList = passwordConfigService.findByStorageId(fromStorageId);
        passwordConfigList.forEach(passwordConfig -> {
            passwordConfig.setId(null);
            passwordConfig.setStorageId(newStorageId);
        });
        passwordConfigService.batchSave(newStorageId, passwordConfigList);
        log.info("复制存储源密码配置成功, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());

        // 复制存储源 readme 配置
        List<ReadmeConfig> readmeConfigList = readmeConfigService.findByStorageId(fromStorageId);
        readmeConfigList.forEach(readmeConfig -> {
            readmeConfig.setId(null);
            readmeConfig.setStorageId(newStorageId);
        });
        readmeConfigService.batchSave(newStorageId, readmeConfigList);
        log.info("复制存储源 readme 配置成功, id: {}, name: {}, config size: {}",
                dbSaveResult.getId(), dbSaveResult.getName(), storageSourceConfigList.size());

        this.checkDav(dbSaveResult);
        return dbSaveResult.getId();
    }
    // 修改
    public void checkDav(StorageSource storageSource, SaveStorageSourceRequest saveStorageSourceRequest,StorageSourceDTO old) {
        if(storageSource.getType().equals(StorageTypeEnum.LOCAL)){
            if(old == null){// 新增存储源
                if(storageSource.getEnable()){
                    webdavHandler.initDavPath();
                }
            }else{
                String webpath = StringUtils.isEmpty(storageSource.getKey())?storageSource.getName() : storageSource.getKey();
                String oldWebpath = StringUtils.isEmpty(old.getKey())?old.getName() : old.getKey();
                if(!storageSource.getEnable().equals(old.isEnable()) ||
                    !webpath.equals(oldWebpath) ||
                    !saveStorageSourceRequest.getStorageSourceAllParam().getEnableWebdav().equals(old.getStorageSourceAllParam().getEnableWebdav()) ||
                    !saveStorageSourceRequest.getStorageSourceAllParam().getFilePath().equals(old.getStorageSourceAllParam().getFilePath())){
                    // 启用、文件路径、访问路径变化
                    webdavHandler.initDavPath();
                }
            }
        }
    }
    public void checkDav(StorageSource storageSource) {
        if(storageSource.getType().equals(StorageTypeEnum.LOCAL)){
            // 新增、删除、复制
            webdavHandler.initDavPath();
        }
    }
}