package com.monkcommerce.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCouponException extends RuntimeException {
    
    public InvalidCouponException(String message) {
        super(message);
    }
    
    public InvalidCouponException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidCouponException(String field, String value) {
        super(String.format("Invalid coupon %s: %s", field, value));
    }
}
