package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.entity.OrderGoods;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.mapper.OrderGoodsMapper;
import com.mengyi.service.OrderGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengyi.vo.OrderQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-18
 */
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements OrderGoodsService {

    @Override
    public void addNewGoods(OrderGoods orderGoods) {
        baseMapper.insert(orderGoods);
    }

    @Override
    public List<OrderGoods> getOrderInfo(String orderId, OrderQuery orderQuery) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderId);
        if(orderQuery != null && !StringUtils.isEmpty(orderQuery.getTitle())){
            wrapper.like("good_title", orderQuery.getTitle());
        }
        List<OrderGoods> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public void removeOrderGood(String id) {
        //删除课程本身
        int result = baseMapper.deleteById(id);
        if(result == 0){
            throw new GuliException(20001, "删除失败");
        }
    }
}
