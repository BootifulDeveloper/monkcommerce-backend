package com.monkcommerce.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a shopping cart containing multiple {@link CartItem}s.
 * <p>
 * The cart keeps track of:
 * <ul>
 * <li>{@code totalPrice} – total price of all items before discounts</li>
 * <li>{@code totalDiscount} – total discount applied across items or
 * cart-wide</li>
 * <li>{@code finalPrice} – payable amount after discounts</li>
 * </ul>
 * </p>
 * <p>
 * This is a plain domain object and not a JPA entity. It is typically used as
 * input/output in services and REST APIs.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

	/**
	 * List of items in the cart.
	 */
	private List<CartItem> items;

	/**
	 * Total price of all items before any discount.
	 */
	private Double totalPrice = 0.0;

	/**
	 * Total discount applied across all items or cart-wide.
	 */
	private Double totalDiscount = 0.0;

	/**
	 * Final payable price after applying discounts.
	 */
	private Double finalPrice = 0.0;

	/**
	 * Calculates the total price of the cart based on the sum of each item's price
	 * × quantity.
	 * <p>
	 * Does not include discounts. To get the final payable amount, subtract
	 * {@link #totalDiscount} from this value.
	 * </p>
	 *
	 * @return total price before discounts
	 */
	public Double calculateTotalPrice() {
		return items == null ? 0.0 : items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
	}
}
