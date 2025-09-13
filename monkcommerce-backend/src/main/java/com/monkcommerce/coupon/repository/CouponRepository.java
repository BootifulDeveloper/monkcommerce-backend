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

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

	/**
	 * Find all active coupons
	 */
	List<Coupon> findByIsActiveTrue();

	/**
	 * Find all active coupons that haven't expired
	 */
	@Query("SELECT c FROM Coupon c WHERE c.isActive = true AND (c.expirationDate IS NULL OR c.expirationDate > :currentTime)")
	List<Coupon> findActiveAndNotExpired(@Param("currentTime") LocalDateTime currentTime);

	/**
	 * Find coupons by type
	 */
	List<Coupon> findByType(CouponType type);

	/**
	 * Find active coupons by type
	 */
	List<Coupon> findByTypeAndIsActiveTrue(CouponType type);

	/**
	 * Find active and not expired coupons by type
	 */
	@Query("SELECT c FROM Coupon c WHERE c.type = :type AND c.isActive = true AND (c.expirationDate IS NULL OR c.expirationDate > :currentTime)")
	List<Coupon> findByTypeAndActiveAndNotExpired(@Param("type") CouponType type,
			@Param("currentTime") LocalDateTime currentTime);

	/**
	 * Find coupons that are about to expire (within specified days)
	 */
	@Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.expirationDate IS NOT NULL AND c.expirationDate BETWEEN :now AND :futureDate")
	List<Coupon> findCouponsExpiringWithin(@Param("now") LocalDateTime now,
			@Param("futureDate") LocalDateTime futureDate);

	/**
	 * Find expired coupons
	 */
	@Query("SELECT c FROM Coupon c WHERE c.expirationDate IS NOT NULL AND c.expirationDate < :currentTime")
	List<Coupon> findExpiredCoupons(@Param("currentTime") LocalDateTime currentTime);

	/**
	 * Count active coupons
	 */
	long countByIsActiveTrue();

	/**
	 * Count coupons by type
	 */
	long countByType(CouponType type);

	/**
	 * Find coupon by ID only if active
	 */
	Optional<Coupon> findByIdAndIsActiveTrue(Long id);

	/**
	 * Check if coupon exists and is active
	 */
	boolean existsByIdAndIsActiveTrue(Long id);

	/**
	 * Soft delete - mark coupon as inactive
	 */
	@Query("UPDATE Coupon c SET c.isActive = false, c.updatedAt = :currentTime WHERE c.id = :id")
	void softDeleteById(@Param("id") Long id, @Param("currentTime") LocalDateTime currentTime);



}
