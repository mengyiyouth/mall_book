package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.R;
import com.mengyi.client.orderclient;
import com.mengyi.client.uclient;
import com.mengyi.entity.StatisticsDaily;
import com.mengyi.mapper.StatisticsDailyMapper;
import com.mengyi.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-02-08
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private uclient ucl;

    @Autowired
    private orderclient oc;

    @Override
    public void registerCount(String day) {
        //添加记录之前先删除相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);
        //远程调用得到某一天注册人数
        R result = ucl.countRegister(day);
        Integer countRegister = 0;
        if(result.getData().get("countRegister") != null)
            countRegister = (Integer)result.getData().get("countRegister");

        //获取到的数据添加到统计分析表中
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);//统计日期

        //远程调用获得一天的图书销售情况
        R r = oc.countOrderDaily(day);
        Integer sale = 0;
        if(r.getData().get("countSale") != null)
            sale = (Integer)r.getData().get("countSale");;
        sta.setOrderNum(sale);

//        sta.setVideoViewNum(RandomUtils.nextInt(100));
//        sta.setLoginNum(RandomUtils.nextInt(100));
//        sta.setCourseNum(RandomUtils.nextInt(100));
        baseMapper.insert(sta);
    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        //根据条件查询对应数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);
        wrapper.orderByAsc("date_calculated");
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);
        //因为返回有两部分数据  日期  日期对应数量

        //前端要求数据json结构 对应后端Java代码是list集合
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            //封装日期list集合
            date_calculatedList.add(daily.getDateCalculated());
            //封装数量
            switch (type) {
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "order_num":
                    numDataList.add(daily.getOrderNum());
                    break;
                default:
                    break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculatedList", date_calculatedList);
        map.put("numDataList", numDataList);

        return map;
    }
}
