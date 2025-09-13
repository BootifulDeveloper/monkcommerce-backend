package com.monkcommerce.coupon.service.strategy;

import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CartWiseCouponStrategy implements CouponStrategy {

	@Override
	public boolean isApplicable(Cart cart, Coupon coupon) {
		double threshold = Double.parseDouble(coupon.getDetails().get("threshold"));
		return cart.calculateTotalPrice() > threshold;
	}

	@Override
	public double calculateDiscount(Cart cart, Coupon coupon) {
		if (!isApplicable(cart, coupon)) {
			return 0.0;
		}

		double discountPercentage = Double.parseDouble(coupon.getDetails().get("discount"));
		return cart.calculateTotalPrice() * (discountPercentage / 100);
	}

	@Override
	public Cart applyCoupon(Cart cart, Coupon coupon) {
		double discount = calculateDiscount(cart, coupon);

		Cart updatedCart = new Cart();
		updatedCart.setItems(cart.getItems());
		updatedCart.setTotalPrice(cart.calculateTotalPrice());
		updatedCart.setTotalDiscount(discount);
		updatedCart.setFinalPrice(cart.calculateTotalPrice() - discount);

		return updatedCart;
	}
}