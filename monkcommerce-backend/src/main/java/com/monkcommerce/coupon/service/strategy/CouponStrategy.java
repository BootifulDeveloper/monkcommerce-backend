package com.monkcommerce.coupon.service.strategy;

import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;

/**
 * Strategy interface for applying coupon business rules.
 * <p>
 * Each {@link Coupon} type should have a dedicated implementation of this
 * interface that defines:
 * <ul>
 * <li>Whether the coupon is applicable to a given {@link Cart}</li>
 * <li>How the discount is calculated</li>
 * <li>How the coupon is applied to update the cart totals</li>
 * </ul>
 * This follows the <b>Strategy Pattern</b>, allowing new coupon types to be
 * added without modifying existing business logic.
 * </p>
 */
public interface CouponStrategy {

	/**
	 * Determines whether the given coupon can be applied to the cart.
	 *
	 * @param cart   the {@link Cart} containing items and totals
	 * @param coupon the {@link Coupon} to validate
	 * @return {@code true} if the coupon is applicable; {@code false} otherwise
	 */
	boolean isApplicable(Cart cart, Coupon coupon);

	/**
	 * Calculates the discount amount for the given cart if the coupon is
	 * applicable.
	 *
	 * @param cart   the {@link Cart} containing items and totals
	 * @param coupon the {@link Coupon} for which to calculate the discount
	 * @return the discount amount (≥ 0). Returns {@code 0.0} if not applicable
	 */
	double calculateDiscount(Cart cart, Coupon coupon);

	/**
	 * Applies the coupon to the given cart, updating totals and discounts.
	 *
	 * @param cart   the {@link Cart} to which the coupon will be applied
	 * @param coupon the {@link Coupon} being applied
	 * @return the updated {@link Cart} with discounts applied
	 * @throws com.monkcommerce.coupon.exception.InvalidCouponException if the
	 *                                                                  coupon
	 *                                                                  cannot be
	 *                                                                  applied
	 */
	Cart applyCoupon(Cart cart, Coupon coupon);
}
