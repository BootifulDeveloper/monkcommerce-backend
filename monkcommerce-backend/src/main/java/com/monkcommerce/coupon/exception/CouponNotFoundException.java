package com.monkcommerce.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CouponNotFoundException extends RuntimeException {

	public CouponNotFoundException(String message) {
		super(message);
	}

	public CouponNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponNotFoundException(Long couponId) {
		super(String.format("Coupon not found with ID: %d", couponId));
	}
}