package com.monkcommerce.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CouponNotApplicableException extends RuntimeException {
    
    public CouponNotApplicableException(String message) {
        super(message);
    }
    
    public CouponNotApplicableException(Long couponId) {
        super(String.format("Coupon with ID %d is not applicable to this cart", couponId));
    }
    
    public CouponNotApplicableException(Long couponId, String reason) {
        super(String.format("Coupon with ID %d is not applicable: %s", couponId, reason));
    }
}
