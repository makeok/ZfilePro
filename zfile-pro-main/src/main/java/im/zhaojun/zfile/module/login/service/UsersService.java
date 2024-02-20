package im.zhaojun.zfile.module.login.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import im.zhaojun.zfile.core.exception.ServiceException;
import im.zhaojun.zfile.core.util.CodeMsg;
import im.zhaojun.zfile.module.login.mapper.UsersMapper;
import im.zhaojun.zfile.module.login.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 存储源基本信息 Service
 *
 * @author zhaojun
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "users")
public class UsersService {
    @Resource
    UsersMapper usersMapper;

    @Cacheable(key = "'selectAll'")
    public List<User> selectAll() {
        return usersMapper.selectAll();
    }

    /**
     * 获取指定用户，不含password字段
     *
     * @param id 存储源 ID
     * @return 存储源设置
     */
    @Cacheable(key = "'findOneById' + #id")
    public User findOneById(Integer id) {
        return usersMapper.findOneById(id);
    }

    // 获取指定用户，含password字段
    @Cacheable(key = "'selectById' + #id")
    public User selectById(Integer id) {
        return usersMapper.selectById(id);
    }

    @Cacheable(key = "'findOneByUsername' + #username")
    public User findOneByUsername(String username) {
        LambdaQueryWrapper<User> query = Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username);
        User user = usersMapper.selectOne(query);
        return user;
    }

    /**
     * 删除指定存储源设置, 会级联删除其参数设置
     *
     * @param   id
     *          存储源 ID
     */
    @Caching(evict = {
            @CacheEvict(key = "'selectById' + #id"),
            @CacheEvict(key = "'findOneById' + #id"),
            @CacheEvict(key = "'findOneByUsername' + #result.username"),
            @CacheEvict(key = "'selectAll'")
    })
    public User deleteById(Integer id) {
        log.info("删除 id 为 {} 的用户", id);
        User user = findOneById(id);
        
        if (user == null) {
            String msg = StrUtil.format("删除用户时检测到 id 为 {} 的用户不存在", id);
            throw new ServiceException(CodeMsg.USER_NOT_EXIST);
        }

        usersMapper.deleteById(id);

        log.info("尝试删除用户 {} 成功", id);

        return user;
    }

    @Caching(evict = {
            @CacheEvict(key = "'selectById' + #entity.id"),
            @CacheEvict(key = "'findOneById' + #entity.id"),
            @CacheEvict(key = "'findOneByUsername' + #entity.username"),
            @CacheEvict(key = "'selectAll'")
    })
    public void updateFieldsById(User entity) {
        usersMapper.updateFieldsById(entity);
    }

    @Caching(evict = {
            @CacheEvict(key = "'selectById' + #entity.id"),
            @CacheEvict(key = "'findOneById' + #entity.id"),
            @CacheEvict(key = "'findOneByUsername' + #entity.username"),
            @CacheEvict(key = "'selectAll'")
    })
    public void updatePasswdById(User entity) {
        usersMapper.updatePasswdById(entity);
    }

    @Caching(evict = {
            @CacheEvict(key = "'selectById' + #entity.id"),
            @CacheEvict(key = "'findOneById' + #entity.id"),
            @CacheEvict(key = "'findOneByUsername' + #entity.username"),
            @CacheEvict(key = "'selectAll'")
    })
    public void updateEnabledById(User entity) {
        usersMapper.updateEnabledById(entity);
    }

    
    /**
     * 保存存储源基本信息及其对应的参数设置
     *
     * @param entity 存储源 DTO 对象
     */
    @Caching(evict = {
            @CacheEvict(key = "'selectById' + #entity.id"),
            @CacheEvict(key = "'findOneById' + #entity.id"),
            @CacheEvict(key = "'findOneByUsername' + #result.username"),
            @CacheEvict(key = "'selectAll'")
    })
    @Transactional(rollbackFor = Exception.class)
    public User save(User entity) {
        log.info("尝试保存用户, id: {}, name: {}", entity.getId(), entity.getUsername());

        // 保存存储源基本信息
        if (entity.getId() == null) {
            entity.setPassword(SecureUtil.sha256(entity.getPassword()));
            User query = findOneByUsername(entity.getUsername());
            if(query == null) {
                usersMapper.insert(entity);
                entity = findOneByUsername(entity.getUsername());
            }else{
                entity.setId(query.getId());
                usersMapper.updateFieldsById(entity);
            }
        } else {
            usersMapper.updateFieldsById(entity);
        }
        log.info("保存用户成功, id: {}, name: {}", entity.getId(), entity.getUsername());

        return entity;
    }

    /**
     * 保存存储源基本信息及其对应的参数设置
     *
     * @param   entity
     *          存储源 DTO 对象
     */
    @Caching(evict = {
            @CacheEvict(key = "'selectById' + #entity.id"),
            @CacheEvict(key = "'findOneById' + #entity.id"),
            @CacheEvict(key = "'findOneByUsername' + #entity.username"),
            @CacheEvict(key = "'selectAll'")
    })
    @Transactional(rollbackFor = Exception.class)
    public Integer saveUserInfo(User entity) {
        log.info("尝试保存用户, id: {}, name: {}", entity.getId(), entity.getUsername());
        usersMapper.updateZhById(entity);
        log.info("保存用户成功, id: {}, name: {}", entity.getId(), entity.getUsername());
        return entity.getId();
    }


    public boolean existByUsername(String username) {
        User user = findOneByUsername(username);
        return user==null ? false: true;
    }
}