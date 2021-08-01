package com.mengyi.service;

import com.mengyi.entity.AclUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-03-22
 */
public interface AclUserService extends IService<AclUser> {

    String login(AclUser aclUser);
}
