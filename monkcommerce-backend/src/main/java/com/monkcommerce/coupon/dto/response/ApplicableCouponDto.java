package com.monkcommerce.coupon.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApplicableCouponDto {
	private Long couponId;
	private String type;
	private Double discount;
}
