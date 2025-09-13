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
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CouponType type;

	@ElementCollection
	@CollectionTable(name = "coupon_details")
	@MapKeyColumn(name = "detail_key")
	@Column(name = "detail_value")
	private Map<String, String> details;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "expiration_date")
	private LocalDateTime expirationDate;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}
