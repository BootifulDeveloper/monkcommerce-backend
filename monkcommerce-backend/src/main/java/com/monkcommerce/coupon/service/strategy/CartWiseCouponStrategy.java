package com.monkcommerce.coupon.service.strategy;

import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for cart-wide coupons.
 * <p>
 * This strategy applies a percentage discount to the entire {@link Cart} if the
 * total cart value exceeds a given threshold. The {@link Coupon} is expected to
 * contain:
 * <ul>
 * <li><b>threshold</b> - the minimum cart total required for the coupon to be
 * applicable</li>
 * <li><b>discount</b> - the discount percentage (e.g., {@code 15} for 15%)</li>
 * </ul>
 * </p>
 */
@Component
public class CartWiseCouponStrategy implements CouponStrategy {

	/**
	 * Checks if the coupon is applicable to the given cart.
	 * <p>
	 * A cart-wide coupon is applicable if the total price of the cart is greater
	 * than the threshold specified in the coupon details.
	 * </p>
	 *
	 * @param cart   the {@link Cart} containing items
	 * @param coupon the {@link Coupon} with threshold and discount details
	 * @return {@code true} if the cart total exceeds the threshold, {@code false}
	 *         otherwise
	 */
	@Override
	public boolean isApplicable(Cart cart, Coupon coupon) {
		double threshold = Double.parseDouble(coupon.getDetails().get("threshold"));
		return cart.calculateTotalPrice() > threshold;
	}

	/**
	 * Calculates the discount amount for the given cart if the coupon is
	 * applicable.
	 *
	 * @param cart   the {@link Cart} containing items
	 * @param coupon the {@link Coupon} with discount details
	 * @return the discount amount based on the cart total; {@code 0.0} if not
	 *         applicable
	 */
	@Override
	public double calculateDiscount(Cart cart, Coupon coupon) {
		if (!isApplicable(cart, coupon)) {
			return 0.0;
		}

		double discountPercentage = Double.parseDouble(coupon.getDetails().get("discount"));
		return cart.calculateTotalPrice() * (discountPercentage / 100);
	}

	/**
	 * Applies the coupon to the given cart.
	 * <p>
	 * The discount is applied to the entire cart if the threshold condition is met.
	 * The {@link Cart}'s total price, total discount, and final price are updated
	 * accordingly.
	 * </p>
	 *
	 * @param cart   the original {@link Cart}
	 * @param coupon the {@link Coupon} with threshold and discount details
	 * @return a new {@link Cart} instance with updated totals
	 */
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
