package com.monkcommerce.coupon.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for updating an existing
 * {@link com.monkcommerce.coupon.entity.Coupon}.
 * <p>
 * All fields are optional. Only the provided fields will be updated in the
 * database.
 * </p>
 */
@Data
public class UpdateCouponDto {

	/**
	 * Optional type of the coupon (e.g., "cart-wise", "product-wise", "bxgy").
	 * <p>
	 * If provided, the coupon type will be updated.
	 * </p>
	 */
	private String type;

	/**
	 * Optional dynamic key-value details for coupon configuration.
	 * <p>
	 * Example:
	 * <ul>
	 * <li>{@code {"discount": 15}}</li>
	 * <li>{@code {"product_id": 123}}</li>
	 * </ul>
	 * If provided, it will replace the existing coupon details.
	 * </p>
	 */
	private Map<String, Object> details;

	/**
	 * Optional expiration date of the coupon.
	 * <p>
	 * If provided, it will update the coupon's expiration date.
	 * </p>
	 */
	private LocalDateTime expirationDate;

	/**
	 * Optional flag indicating whether the coupon is active.
	 * <p>
	 * If provided, the coupon's active status will be updated.
	 * </p>
	 */
	private Boolean isActive;
}
