package com.monkcommerce.coupon.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CartDto {
 @NotEmpty(message = "Cart items cannot be empty")
 private List<CartItemDto> items;
 
 @Data
 public static class CartItemDto {
     private Long productId;
     private Integer quantity;
     private Double price;
 }
}
