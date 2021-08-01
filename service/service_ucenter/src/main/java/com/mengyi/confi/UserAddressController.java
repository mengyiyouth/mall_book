package com.mengyi.confi;


import com.mengyi.JwtUtils;
import com.mengyi.R;
import com.mengyi.entity.UserAddress;
import com.mengyi.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/eduucenter/address")
public class UserAddressController {
    @Autowired
    UserAddressService userAddressService;
    //    新增地址
    @PostMapping("/addAddress")
    public R saveAddress(@RequestBody UserAddress userAddress, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        userAddress.setMemberId(memberId);
        userAddressService.save(userAddress);
        return R.ok();
    }

    //    更新地址
    @PostMapping("/updateAddress")
    public R updateAddress(@RequestBody UserAddress userAddress) {
        userAddressService.updateById(userAddress);
        return R.ok();
    }

    //    删除地址信息
    @PostMapping("/deleteAddress")
    public R deleteAddress(@RequestBody UserAddress userAddress) {
        userAddressService.removeById(userAddress.getId());
        return R.ok();
    }

    //得到所有地址
    @GetMapping("/getAllAddress")
    public R getAllAddressList(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        List<UserAddress> allAddress = userAddressService.getAllAddress(memberId);
        return R.ok().data("list", allAddress);
    }
}

