package com.monkcommerce.coupon.service.strategy;

import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.CartItem;
import com.monkcommerce.coupon.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class ProductWiseCouponStrategy implements CouponStrategy {

	@Override
	public boolean isApplicable(Cart cart, Coupon coupon) {
		Long productId = Long.parseLong(coupon.getDetails().get("product_id"));
		return cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId));
	}

	@Override
	public double calculateDiscount(Cart cart, Coupon coupon) {
		if (!isApplicable(cart, coupon)) {
			return 0.0;
		}

		Long productId = Long.parseLong(coupon.getDetails().get("product_id"));
		double discountPercentage = Double.parseDouble(coupon.getDetails().get("discount"));

		return cart.getItems().stream().filter(item -> item.getProductId().equals(productId))
				.mapToDouble(item -> item.getPrice() * item.getQuantity() * (discountPercentage / 100)).sum();
	}

	@Override
	public Cart applyCoupon(Cart cart, Coupon coupon) {
		Long productId = Long.parseLong(coupon.getDetails().get("product_id"));
		double discountPercentage = Double.parseDouble(coupon.getDetails().get("discount"));

		Cart updatedCart = new Cart();
		updatedCart.setItems(cart.getItems());

		double totalDiscount = 0.0;
		for (CartItem item : updatedCart.getItems()) {
			if (item.getProductId().equals(productId)) {
				double itemDiscount = item.getPrice() * item.getQuantity() * (discountPercentage / 100);
				item.setTotalDiscount(itemDiscount);
				totalDiscount += itemDiscount;
			}
		}

		updatedCart.setTotalPrice(cart.calculateTotalPrice());
		updatedCart.setTotalDiscount(totalDiscount);
		updatedCart.setFinalPrice(cart.calculateTotalPrice() - totalDiscount);

		return updatedCart;
	}
}