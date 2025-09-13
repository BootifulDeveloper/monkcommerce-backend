package com.monkcommerce.coupon.service;

import java.util.List;

import com.monkcommerce.coupon.dto.request.CreateCouponDto;
import com.monkcommerce.coupon.dto.request.UpdateCouponDto;
import com.monkcommerce.coupon.dto.response.ApplicableCouponDto;
import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.enums.CouponType;

/**
 * Service interface for managing {@link Coupon} lifecycle and operations.
 * <p>
 * Provides methods to create, update, delete, retrieve, and apply coupons. Also
 * includes business logic for validating and filtering applicable coupons for a
 * given {@link Cart}.
 * </p>
 */
public interface CouponService {

	/**
	 * Creates and persists a new coupon.
	 *
	 * @param createCouponDto the DTO containing coupon type, details, and
	 *                        expiration date
	 * @return the created {@link Coupon}
	 */
	Coupon createCoupon(CreateCouponDto createCouponDto);

	/**
	 * Retrieves all coupons.
	 *
	 * @return a list of all {@link Coupon}
	 */
	List<Coupon> getAllCoupons();

	/**
	 * Retrieves a coupon by its unique ID.
	 *
	 * @param id the coupon ID
	 * @return the {@link Coupon} if found
	 * @throws com.monkcommerce.coupon.exception.CouponNotFoundException if no
	 *                                                                   coupon
	 *                                                                   exists with
	 *                                                                   the given
	 *                                                                   ID
	 */
	Coupon getCouponById(Long id);

	/**
	 * Updates an existing coupon with new details.
	 *
	 * @param id              the coupon ID
	 * @param updateCouponDto the DTO containing updated details
	 * @return the updated {@link Coupon}
	 * @throws com.monkcommerce.coupon.exception.CouponNotFoundException if no
	 *                                                                   coupon
	 *                                                                   exists with
	 *                                                                   the given
	 *                                                                   ID
	 */
	Coupon updateCoupon(Long id, UpdateCouponDto updateCouponDto);

	/**
	 * Deletes a coupon by its ID.
	 *
	 * @param id the coupon ID
	 * @throws com.monkcommerce.coupon.exception.CouponNotFoundException if no
	 *                                                                   coupon
	 *                                                                   exists with
	 *                                                                   the given
	 *                                                                   ID
	 */
	void deleteCoupon(Long id);

	/**
	 * Retrieves all coupons applicable to a given cart.
	 *
	 * @param cart the {@link Cart} containing items and totals
	 * @return a list of {@link ApplicableCouponDto} representing eligible coupons
	 */
	List<ApplicableCouponDto> getApplicableCoupons(Cart cart);

	/**
	 * Applies a coupon to the given cart if valid and applicable.
	 *
	 * @param couponId the coupon ID
	 * @param cart     the {@link Cart} to which the coupon will be applied
	 * @return the updated {@link Cart} with discounts applied
	 * @throws com.monkcommerce.coupon.exception.CouponNotFoundException if the
	 *                                                                   coupon does
	 *                                                                   not exist
	 *                                                                   or is
	 *                                                                   inactive
	 * @throws com.monkcommerce.coupon.exception.InvalidCouponException  if the
	 *                                                                   coupon is
	 *                                                                   expired,
	 *                                                                   invalid, or
	 *                                                                   not
	 *                                                                   applicable
	 */
	Cart applyCoupon(Long couponId, Cart cart);

	/**
	 * Retrieves all active coupons of a specific type.
	 *
	 * @param type the {@link CouponType}
	 * @return a list of active {@link Coupon} of the given type
	 */
	List<Coupon> getActiveCouponsByType(CouponType type);

	/**
	 * Retrieves coupons that will expire within the given number of days.
	 *
	 * @param days the number of days from the current date
	 * @return a list of {@link Coupon} expiring within the given timeframe
	 */
	List<Coupon> getCouponsExpiringWithin(int days);
}
