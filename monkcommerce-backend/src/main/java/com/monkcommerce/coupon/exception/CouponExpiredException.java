package com.monkcommerce.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CouponExpiredException extends RuntimeException {
    
    public CouponExpiredException(String message) {
        super(message);
    }
    
    public CouponExpiredException(Long couponId) {
        super(String.format("Coupon with ID %d has expired", couponId));
    }
    
    public CouponExpiredException(Long couponId, LocalDateTime expirationDate) {
        super(String.format("Coupon with ID %d expired on %s", couponId, expirationDate));
    }
}