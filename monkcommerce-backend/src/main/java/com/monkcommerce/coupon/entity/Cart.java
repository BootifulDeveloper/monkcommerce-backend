package com.monkcommerce.coupon.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
	private List<CartItem> items;
	private Double totalPrice = 0.0;
	private Double totalDiscount = 0.0;
	private Double finalPrice = 0.0;

	public Double calculateTotalPrice() {
		return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
	}
}