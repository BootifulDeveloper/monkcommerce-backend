package com.monkcommerce.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a coupon that is applicable for a
 * given {@code Cart}.
 * <p>
 * This object is typically returned in API responses when listing coupons that
 * can be applied to a cart.
 * </p>
 *
 * <p>
 * Contains:
 * </p>
 * <ul>
 * <li>{@code couponId} – unique identifier of the coupon</li>
 * <li>{@code type} – type of coupon (e.g., cart-wise, product-wise, bxgy)</li>
 * <li>{@code discount} – discount amount or percentage calculated for the
 * cart</li>
 * </ul>
 */
@Data
@AllArgsConstructor
public class ApplicableCouponDto {

	/**
	 * Unique identifier of the coupon.
	 */
	private Long couponId;

	/**
	 * Type of the coupon (e.g., "cart-wise", "product-wise", "bxgy").
	 */
	private String type;

	/**
	 * Discount amount or percentage that the coupon provides.
	 */
	private Double discount;
}
