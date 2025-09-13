package com.monkcommerce.coupon.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UpdateCouponDto {
	private String type;
	private Map<String, Object> details;
	private LocalDateTime expirationDate;
	private Boolean isActive;
}