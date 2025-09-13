package com.monkcommerce.coupon.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a shopping cart submitted by the
 * client for coupon evaluation or checkout.
 * <p>
 * Contains a list of cart items, each with product information and pricing.
 * Validation ensures that the cart cannot be empty.
 * </p>
 */
@Data
public class CartDto {

	/**
	 * List of items in the cart. Must not be empty.
	 */
	@NotEmpty(message = "Cart items cannot be empty")
	private List<CartItemDto> items;

	/**
	 * Represents a single item in the {@link CartDto}.
	 */
	@Data
	public static class CartItemDto {

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
	}
}
