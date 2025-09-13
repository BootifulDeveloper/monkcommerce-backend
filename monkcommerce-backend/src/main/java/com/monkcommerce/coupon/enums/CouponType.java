package com.monkcommerce.coupon.enums;

import java.util.Arrays;

/**
 * Enum representing different types of coupons supported in the system.
 * <p>
 * Each type is mapped to a string value for easier storage and retrieval.
 * </p>
 */
public enum CouponType {

	/**
	 * Coupon applied to the entire cart value.
	 */
	CART_WISE("cart-wise"),

	/**
	 * Coupon applied to a specific product in the cart.
	 */
	PRODUCT_WISE("product-wise"),

	/**
	 * Buy X Get Y type of coupon.
	 */
	BXGY("bxgy");

	private final String value;

	CouponType(String value) {
		this.value = value;
	}

	/**
	 * Gets the string value associated with this coupon type.
	 *
	 * @return string representation of the coupon type
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Converts a string value to the corresponding {@link CouponType}.
	 * <p>
	 * Matching is case-insensitive.
	 * </p>
	 *
	 * @param value the string representation of the coupon type
	 * @return matching {@link CouponType}
	 * @throws IllegalArgumentException if no matching coupon type is found
	 */
	public static CouponType fromString(String value) {
		return Arrays.stream(CouponType.values()).filter(type -> type.value.equalsIgnoreCase(value)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown coupon type: " + value));
	}
}
