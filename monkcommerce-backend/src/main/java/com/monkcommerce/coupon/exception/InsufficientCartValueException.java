
package com.monkcommerce.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientCartValueException extends RuntimeException {
    
    public InsufficientCartValueException(String message) {
        super(message);
    }
    
    public InsufficientCartValueException(double cartValue, double requiredValue) {
        super(String.format("Cart value %.2f is insufficient. Required minimum: %.2f", cartValue, requiredValue));
    }
    
    public InsufficientCartValueException(Long couponId, double cartValue, double requiredValue) {
        super(String.format("Coupon %d requires minimum cart value of %.2f, but cart value is %.2f", 
              couponId, requiredValue, cartValue));
    }
}