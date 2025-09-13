package com.monkcommerce.coupon.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.monkcommerce.coupon.enums.CouponType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a coupon in the system.
 * <p>
 * Coupons can be of different {@link CouponType} and may carry additional
 * dynamic details stored in a key-value format.
 * </p>
 * <p>
 * Common queries involve filtering by:
 * <ul>
 * <li>{@code type} – e.g., cart-wise, product-wise, bxgy</li>
 * <li>{@code isActive} – whether the coupon is usable</li>
 * <li>{@code expirationDate} – to check if the coupon is still valid</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "coupons", indexes = { @Index(name = "idx_coupon_type", columnList = "type"),
		@Index(name = "idx_coupon_is_active", columnList = "is_active"),
		@Index(name = "idx_coupon_expiration", columnList = "expiration_date"),
		@Index(name = "idx_coupon_type_active", columnList = "type,is_active"),
		@Index(name = "idx_coupon_active_expiration", columnList = "is_active,expiration_date") }, uniqueConstraints = {
				// Ensures no duplicate active coupon of the same type with same expiration date
				@jakarta.persistence.UniqueConstraint(name = "uk_coupon_type_active_expiration", columnNames = { "type",
						"is_active", "expiration_date" }) })
@Data
public class Coupon {

	/**
	 * Primary key – unique identifier for each coupon.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * The type of coupon (e.g., cart-wise, product-wise, bxgy).
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private CouponType type;

	/**
	 * Dynamic key-value details for coupon configuration. Example:
	 * <ul>
	 * <li>{@code {"discount": "10"}} – 10% discount</li>
	 * <li>{@code {"product_id": "123"}} – applies to product 123</li>
	 * </ul>
	 */
	@ElementCollection
	@CollectionTable(name = "coupon_details")
	@MapKeyColumn(name = "detail_key", length = 100)
	@Column(name = "detail_value", length = 255)
	private Map<String, String> details;

	/**
	 * Indicates whether the coupon is active and usable.
	 */
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	/**
	 * Expiration date of the coupon. If null, the coupon never expires.
	 */
	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	/**
	 * Timestamp when the coupon was created.
	 */
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	/**
	 * Timestamp when the coupon was last updated.
	 */
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt = LocalDateTime.now();
}
