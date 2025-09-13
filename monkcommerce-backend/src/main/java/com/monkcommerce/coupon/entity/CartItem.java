package com.monkcommerce.coupon.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	private Long productId;
	private Integer quantity;
	private Double price;
	private Double totalDiscount = 0.0;
}