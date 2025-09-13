package com.monkcommerce.coupon.controller;

import com.monkcommerce.coupon.dto.request.CartDto;
import com.monkcommerce.coupon.dto.request.CreateCouponDto;
import com.monkcommerce.coupon.dto.request.UpdateCouponDto;
import com.monkcommerce.coupon.dto.response.ApplicableCouponDto;
import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.CartItem;
import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;

	@PostMapping("/coupons")
	public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody CreateCouponDto createCouponDto) {
		Coupon coupon = couponService.createCoupon(createCouponDto);
		return new ResponseEntity<>(coupon, HttpStatus.CREATED);
	}

	@GetMapping("/coupons")
	public ResponseEntity<List<Coupon>> getAllCoupons() {
		List<Coupon> coupons = couponService.getAllCoupons();
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/coupons/{id}")
	public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
		Coupon coupon = couponService.getCouponById(id);
		return ResponseEntity.ok(coupon);
	}

	@DeleteMapping("/coupons/{id}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
		couponService.deleteCoupon(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/applicable-coupons")
	public ResponseEntity<List<ApplicableCouponDto>> getApplicableCoupons(@Valid @RequestBody CartDto cartDto) {
		Cart cart = convertToCart(cartDto);
		List<ApplicableCouponDto> applicableCoupons = couponService.getApplicableCoupons(cart);
		return ResponseEntity.ok(applicableCoupons);
	}

	@PostMapping("/apply-coupon/{id}")
	public ResponseEntity<Cart> applyCoupon(@PathVariable Long id, @Valid @RequestBody CartDto cartDto) {
		Cart cart = convertToCart(cartDto);
		Cart updatedCart = couponService.applyCoupon(id, cart);
		return ResponseEntity.ok(updatedCart);
	}

	private Cart convertToCart(CartDto cartDto) {
		List<CartItem> items = cartDto.getItems().stream()
				.map(itemDto -> new CartItem(itemDto.getProductId(), itemDto.getQuantity(), itemDto.getPrice(), 0.0))
				.toList();
		return new Cart(items, 0.0, 0.0, 0.0);
	}
}