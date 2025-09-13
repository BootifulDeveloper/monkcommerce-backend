package com.monkcommerce.coupon.service.strategy;

import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.CartItem;
import com.monkcommerce.coupon.entity.Coupon;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for product-specific coupons.
 * <p>
 * This strategy applies a percentage discount on a specific product within the
 * {@link Cart}. The {@link Coupon} is expected to contain:
 * <ul>
 * <li><b>product_id</b> - the ID of the product to which the coupon
 * applies</li>
 * <li><b>discount</b> - the discount percentage (e.g., {@code 10} for 10%)</li>
 * </ul>
 * </p>
 */
@Component
public class ProductWiseCouponStrategy implements CouponStrategy {

	/**
	 * Checks if the coupon is applicable to the given cart.
	 * <p>
	 * A product-wise coupon is applicable if the cart contains at least one item
	 * with the product ID specified in the coupon details.
	 * </p>
	 *
	 * @param cart   the {@link Cart} containing items
	 * @param coupon the {@link Coupon} with product-specific details
	 * @return {@code true} if the product is in the cart, {@code false} otherwise
	 */
	@Override
	public boolean isApplicable(Cart cart, Coupon coupon) {
		Long productId = Long.parseLong(coupon.getDetails().get("product_id"));
		return cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId));
	}

	/**
	 * Calculates the discount amount for the given cart if the coupon is
	 * applicable.
	 *
	 * @param cart   the {@link Cart} containing items
	 * @param coupon the {@link Coupon} with product-specific details
	 * @return the total discount for the specified product; {@code 0.0} if not
	 *         applicable
	 */
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

	/**
	 * Applies the coupon to the given cart.
	 * <p>
	 * The discount is applied only to items matching the product ID specified in
	 * the coupon details. The {@link Cart}'s total price, total discount, and final
	 * price are recalculated accordingly.
	 * </p>
	 *
	 * @param cart   the original {@link Cart}
	 * @param coupon the {@link Coupon} with product-specific details
	 * @return a new {@link Cart} instance with updated prices and discounts
	 */
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
