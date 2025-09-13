package com.monkcommerce.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object (DTO) for creating a new
 * {@link com.monkcommerce.coupon.entity.Coupon}.
 * <p>
 * This object is used in API requests to provide all necessary information to
 * create a coupon.
 * </p>
 */
@Data
public class CreateCouponDto {

	/**
	 * Type of the coupon (e.g., "cart-wise", "product-wise", "bxgy").
	 * <p>
	 * Must not be blank.
	 * </p>
	 */
	@NotBlank(message = "Coupon type is required")
	private String type;

	/**
	 * Dynamic key-value details for coupon configuration.
	 * <p>
	 * Example:
	 * <ul>
	 * <li>{@code {"discount": 10}}</li>
	 * <li>{@code {"product_id": 123}}</li>
	 * </ul>
	 * Must not be null.
	 * </p>
	 */
	@NotNull(message = "Coupon details are required")
	private Map<String, Object> details;

	/**
	 * Optional expiration date of the coupon.
	 * <p>
	 * If null, the coupon never expires.
	 * </p>
	 */
	private LocalDateTime expirationDate;
}
