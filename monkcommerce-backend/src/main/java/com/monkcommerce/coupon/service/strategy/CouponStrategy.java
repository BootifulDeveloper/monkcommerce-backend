package com.monkcommerce.coupon.service.strategy;

import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;

public interface CouponStrategy {
	boolean isApplicable(Cart cart, Coupon coupon);

	double calculateDiscount(Cart cart, Coupon coupon);

	Cart applyCoupon(Cart cart, Coupon coupon);
}