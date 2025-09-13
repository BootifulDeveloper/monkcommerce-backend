package com.monkcommerce.coupon.repository;

import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.enums.CouponType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Coupon} entities.
 * <p>
 * Extends:
 * <ul>
 * <li>{@link JpaRepository} - Provides CRUD operations and pagination.</li>
 * <li>{@link JpaSpecificationExecutor} - Enables dynamic query support using
 * Specifications.</li>
 * </ul>
 * </p>
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

	/**
	 * Retrieves all coupons that are currently active.
	 *
	 * @return list of active {@link Coupon} entities
	 */
	List<Coupon> findByIsActiveTrue();

	/**
	 * Retrieves all active coupons that have not expired.
	 * <p>
	 * A coupon is considered valid if:
	 * <ul>
	 * <li>It has no expiration date, or</li>
	 * <li>Its expiration date is greater than the current time.</li>
	 * </ul>
	 * </p>
	 *
	 * @param currentTime current timestamp for filtering
	 * @return list of valid active coupons
	 */
	@Query("SELECT c FROM Coupon c WHERE c.isActive = true AND (c.expirationDate IS NULL OR c.expirationDate > :currentTime)")
	List<Coupon> findActiveAndNotExpired(@Param("currentTime") LocalDateTime currentTime);

	/**
	 * Retrieves all coupons of a given type.
	 *
	 * @param type the {@link CouponType} to filter by
	 * @return list of coupons of the specified type
	 */
	List<Coupon> findByType(CouponType type);

	/**
	 * Retrieves all active coupons of a given type.
	 *
	 * @param type the {@link CouponType} to filter by
	 * @return list of active coupons of the given type
	 */
	List<Coupon> findByTypeAndIsActiveTrue(CouponType type);

	/**
	 * Retrieves all active and non-expired coupons of a given type.
	 *
	 * @param type        the {@link CouponType} to filter by
	 * @param currentTime current timestamp for expiration check
	 * @return list of valid active coupons of the given type
	 */
	@Query("SELECT c FROM Coupon c WHERE c.type = :type AND c.isActive = true AND (c.expirationDate IS NULL OR c.expirationDate > :currentTime)")
	List<Coupon> findByTypeAndActiveAndNotExpired(@Param("type") CouponType type,
			@Param("currentTime") LocalDateTime currentTime);

	/**
	 * Retrieves active coupons that are about to expire within the given time
	 * window.
	 *
	 * @param now        the current timestamp
	 * @param futureDate the future timestamp up to which expiration is considered
	 * @return list of coupons expiring within the specified period
	 */
	@Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.expirationDate IS NOT NULL AND c.expirationDate BETWEEN :now AND :futureDate")
	List<Coupon> findCouponsExpiringWithin(@Param("now") LocalDateTime now,
			@Param("futureDate") LocalDateTime futureDate);

	/**
	 * Retrieves all coupons that are expired (past their expiration date).
	 *
	 * @param currentTime current timestamp for filtering
	 * @return list of expired coupons
	 */
	@Query("SELECT c FROM Coupon c WHERE c.expirationDate IS NOT NULL AND c.expirationDate < :currentTime")
	List<Coupon> findExpiredCoupons(@Param("currentTime") LocalDateTime currentTime);

	/**
	 * Counts the number of active coupons.
	 *
	 * @return the total count of active coupons
	 */
	long countByIsActiveTrue();

	/**
	 * Counts the number of coupons by type.
	 *
	 * @param type the {@link CouponType} to filter by
	 * @return the number of coupons of the given type
	 */
	long countByType(CouponType type);

	/**
	 * Retrieves a coupon by ID only if it is active.
	 *
	 * @param id the ID of the coupon
	 * @return an {@link Optional} containing the active coupon, if found
	 */
	Optional<Coupon> findByIdAndIsActiveTrue(Long id);

	/**
	 * Checks whether a coupon exists with the given ID and is active.
	 *
	 * @param id the coupon ID
	 * @return {@code true} if the coupon exists and is active, {@code false}
	 *         otherwise
	 */
	boolean existsByIdAndIsActiveTrue(Long id);

	/**
	 * Performs a soft delete by marking a coupon as inactive and updating its
	 * {@code updatedAt} timestamp.
	 *
	 * @param id          the coupon ID to deactivate
	 * @param currentTime the timestamp to set as {@code updatedAt}
	 */
	@Query("UPDATE Coupon c SET c.isActive = false, c.updatedAt = :currentTime WHERE c.id = :id")
	void softDeleteById(@Param("id") Long id, @Param("currentTime") LocalDateTime currentTime);
}
