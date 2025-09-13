package com.monkcommerce.coupon.repository.specification;

import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.enums.CouponType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CouponSpecifications {

	/**
	 * Specification to find active coupons
	 */
	public static Specification<Coupon> isActive() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), true);
	}

	/**
	 * Specification to find non-expired coupons
	 */
	public static Specification<Coupon> isNotExpired() {
		return (root, query, criteriaBuilder) -> {
			LocalDateTime now = LocalDateTime.now();
			return criteriaBuilder.or(criteriaBuilder.isNull(root.get("expirationDate")),
					criteriaBuilder.greaterThan(root.get("expirationDate"), now));
		};
	}

	/**
	 * Specification to find coupons by type
	 */
	public static Specification<Coupon> hasType(CouponType type) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
	}

	/**
	 * Specification to find coupons created within date range
	 */
	public static Specification<Coupon> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
	}

	/**
	 * Specification to find coupons expiring within specified days
	 */
	public static Specification<Coupon> expiringWithin(int days) {
		return (root, query, criteriaBuilder) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime futureDate = now.plusDays(days);
			return criteriaBuilder.between(root.get("expirationDate"), now, futureDate);
		};
	}

	/**
	 * Complex specification builder for filtering coupons
	 */
	public static Specification<Coupon> buildCouponFilter(CouponType type, Boolean isActive, Boolean includeExpired,
			LocalDateTime fromDate, LocalDateTime toDate) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Filter by type
			if (type != null) {
				predicates.add(criteriaBuilder.equal(root.get("type"), type));
			}

			// Filter by active status
			if (isActive != null) {
				predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
			}

			// Filter by expiration
			if (includeExpired != null && !includeExpired) {
				LocalDateTime now = LocalDateTime.now();
				predicates.add(criteriaBuilder.or(criteriaBuilder.isNull(root.get("expirationDate")),
						criteriaBuilder.greaterThan(root.get("expirationDate"), now)));
			}

			// Filter by creation date range
			if (fromDate != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
			}
			if (toDate != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
