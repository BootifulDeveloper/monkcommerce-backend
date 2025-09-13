package com.monkcommerce.coupon.service;

import com.monkcommerce.coupon.dto.request.CreateCouponDto;
import com.monkcommerce.coupon.dto.request.UpdateCouponDto;
import com.monkcommerce.coupon.dto.response.ApplicableCouponDto;
import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;
import java.util.List;

public interface CouponService {
    Coupon createCoupon(CreateCouponDto createCouponDto);
    List<Coupon> getAllCoupons();
    Coupon getCouponById(Long id);
    Coupon updateCoupon(Long id, UpdateCouponDto updateCouponDto);
    void deleteCoupon(Long id);
    List<ApplicableCouponDto> getApplicableCoupons(Cart cart);
    Cart applyCoupon(Long couponId, Cart cart);
}
