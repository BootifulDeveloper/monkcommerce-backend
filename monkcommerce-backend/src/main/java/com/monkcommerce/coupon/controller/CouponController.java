package com.monkcommerce.coupon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monkcommerce.coupon.dto.request.CartDto;
import com.monkcommerce.coupon.dto.request.CreateCouponDto;
import com.monkcommerce.coupon.dto.response.ApplicableCouponDto;
import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.CartItem;
import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing coupon-related operations such as creation,
 * retrieval, deletion, applicability checks, and applying coupons to carts.
 * <p>
 * This controller exposes endpoints under the base path <b>/api/v1</b>.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;

	/**
	 * Creates a new coupon.
	 *
	 * @param createCouponDto the request body containing coupon details
	 * @return {@link ResponseEntity} with the created {@link Coupon} and HTTP
	 *         status 201 (Created)
	 */
	@PostMapping("/coupons")
	public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody CreateCouponDto createCouponDto) {
		Coupon coupon = couponService.createCoupon(createCouponDto);
		return new ResponseEntity<>(coupon, HttpStatus.CREATED);
	}

	/**
	 * Retrieves all available coupons.
	 *
	 * @return {@link ResponseEntity} containing a list of {@link Coupon} and HTTP
	 *         status 200 (OK)
	 */
	@GetMapping("/coupons")
	public ResponseEntity<List<Coupon>> getAllCoupons() {
		List<Coupon> coupons = couponService.getAllCoupons();
		return ResponseEntity.ok(coupons);
	}

	/**
	 * Retrieves a coupon by its unique identifier.
	 *
	 * @param id the coupon ID
	 * @return {@link ResponseEntity} containing the {@link Coupon} if found, with
	 *         HTTP status 200 (OK)
	 */
	@GetMapping("/coupons/{id}")
	public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
		Coupon coupon = couponService.getCouponById(id);
		return ResponseEntity.ok(coupon);
	}

	/**
	 * Deletes a coupon by its unique identifier.
	 *
	 * @param id the coupon ID
	 * @return {@link ResponseEntity} with HTTP status 204 (No Content) if deletion
	 *         is successful
	 */
	@DeleteMapping("/coupons/{id}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
		couponService.deleteCoupon(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Retrieves a list of applicable coupons for a given cart.
	 *
	 * @param cartDto the request body containing cart details
	 * @return {@link ResponseEntity} with a list of {@link ApplicableCouponDto} and
	 *         HTTP status 200 (OK)
	 */
	@PostMapping("/applicable-coupons")
	public ResponseEntity<List<ApplicableCouponDto>> getApplicableCoupons(@Valid @RequestBody CartDto cartDto) {
		Cart cart = convertToCart(cartDto);
		List<ApplicableCouponDto> applicableCoupons = couponService.getApplicableCoupons(cart);
		return ResponseEntity.ok(applicableCoupons);
	}

	/**
	 * Applies a specific coupon to a cart.
	 *
	 * @param id      the coupon ID
	 * @param cartDto the request body containing cart details
	 * @return {@link ResponseEntity} with the updated {@link Cart} reflecting
	 *         discounts and totals
	 */
	@PostMapping("/apply-coupon/{id}")
	public ResponseEntity<Cart> applyCoupon(@PathVariable Long id, @Valid @RequestBody CartDto cartDto) {
		Cart cart = convertToCart(cartDto);
		Cart updatedCart = couponService.applyCoupon(id, cart);
		return ResponseEntity.ok(updatedCart);
	}

	/**
	 * Converts a {@link CartDto} into a {@link Cart} entity.
	 * <p>
	 * Initializes discount, subtotal, and total to 0.0. These values will be
	 * recalculated when applying coupons.
	 * </p>
	 *
	 * @param cartDto the cart DTO containing item details
	 * @return the {@link Cart} entity
	 */
	private Cart convertToCart(CartDto cartDto) {
		List<CartItem> items = cartDto.getItems().stream()
				.map(itemDto -> new CartItem(itemDto.getProductId(), itemDto.getQuantity(), itemDto.getPrice(), 0.0))
				.toList();
		return new Cart(items, 0.0, 0.0, 0.0);
	}
}
