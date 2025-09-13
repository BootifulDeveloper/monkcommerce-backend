package com.monkcommerce.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an item inside a shopping cart.
 * <p>
 * A {@code CartItem} contains:
 * <ul>
 * <li>{@code productId} – unique identifier of the product</li>
 * <li>{@code quantity} – number of units of the product in the cart</li>
 * <li>{@code price} – unit price of the product</li>
 * <li>{@code totalDiscount} – total discount applied to this product (default:
 * 0.0)</li>
 * </ul>
 * </p>
 * <p>
 * This class is not a JPA entity but a simple value object used within the
 * {@link com.monkcommerce.coupon.entity.Cart}.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

	/**
	 * Unique identifier of the product.
	 */
	private Long productId;

	/**
	 * Quantity of the product in the cart.
	 */
	private Integer quantity;

	/**
	 * Unit price of the product.
	 */
	private Double price;

	/**
	 * Total discount applied to this product. Defaults to {@code 0.0}.
	 */
	private Double totalDiscount = 0.0;
}
