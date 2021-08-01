package com.mengyi.mapper;

import com.mengyi.entity.TOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-14
 */
public interface TOrderMapper extends BaseMapper<TOrder> {

    Integer getSaleSta(String day);
}
