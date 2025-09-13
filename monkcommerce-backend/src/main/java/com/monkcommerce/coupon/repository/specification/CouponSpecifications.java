package com.monkcommerce.coupon.repository.specification;

import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.enums.CouponType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing reusable JPA {@link Specification} implementations
 * for querying {@link Coupon} entities.
 * <p>
 * These specifications can be combined using {@code Specification.and(...)} and
 * {@code Specification.or(...)} to build dynamic queries.
 * </p>
 */
public class CouponSpecifications {

	/**
	 * Builds a specification to find only active coupons.
	 *
	 * @return a {@link Specification} filtering coupons with
	 *         {@code isActive = true}
	 */
	public static Specification<Coupon> isActive() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), true);
	}

	/**
	 * Builds a specification to find only non-expired coupons.
	 * <p>
	 * A coupon is considered non-expired if:
	 * <ul>
	 * <li>Its expiration date is {@code null} (no expiry)</li>
	 * <li>Or its expiration date is greater than the current date/time</li>
	 * </ul>
	 * </p>
	 *
	 * @return a {@link Specification} filtering out expired coupons
	 */
	public static Specification<Coupon> isNotExpired() {
		return (root, query, criteriaBuilder) -> {
			LocalDateTime now = LocalDateTime.now();
			return criteriaBuilder.or(criteriaBuilder.isNull(root.get("expirationDate")),
					criteriaBuilder.greaterThan(root.get("expirationDate"), now));
		};
	}

	/**
	 * Builds a specification to filter coupons by type.
	 *
	 * @param type the {@link CouponType} to match
	 * @return a {@link Specification} filtering coupons by type
	 */
	public static Specification<Coupon> hasType(CouponType type) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
	}

	/**
	 * Builds a specification to filter coupons created within a date range.
	 *
	 * @param startDate the start date (inclusive)
	 * @param endDate   the end date (inclusive)
	 * @return a {@link Specification} filtering coupons created within the range
	 */
	public static Specification<Coupon> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
	}

	/**
	 * Builds a specification to find coupons expiring within the next {@code days}.
	 *
	 * @param days the number of days from now
	 * @return a {@link Specification} filtering coupons expiring in the given
	 *         window
	 */
	public static Specification<Coupon> expiringWithin(int days) {
		return (root, query, criteriaBuilder) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime futureDate = now.plusDays(days);
			return criteriaBuilder.between(root.get("expirationDate"), now, futureDate);
		};
	}

	/**
	 * Builds a flexible specification with multiple optional filters:
	 * <ul>
	 * <li><b>type</b> - filter by {@link CouponType}</li>
	 * <li><b>isActive</b> - filter by active status</li>
	 * <li><b>includeExpired</b> - exclude expired coupons if {@code false}</li>
	 * <li><b>fromDate</b> - filter coupons created after or equal to this date</li>
	 * <li><b>toDate</b> - filter coupons created before or equal to this date</li>
	 * </ul>
	 *
	 * @param type           the coupon type filter (nullable)
	 * @param isActive       the active status filter (nullable)
	 * @param includeExpired whether to include expired coupons
	 * @param fromDate       creation date lower bound (nullable)
	 * @param toDate         creation date upper bound (nullable)
	 * @return a {@link Specification} with the applied filters
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

			// Filter by expiration (exclude expired if requested)
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
