package com.monkcommerce.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCouponException extends RuntimeException {
    
    public DuplicateCouponException(String message) {
        super(message);
    }
    
//    public DuplicateCouponException(String couponCode) {
//        super(String.format("Coupon with code '%s' already exists", couponCode));
//    }
}
