package com.monkcommerce.coupon.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CreateCouponDto {
	@NotBlank(message = "Coupon type is required")
	private String type;

	@NotNull(message = "Coupon details are required")
	private Map<String, Object> details;

	private LocalDateTime expirationDate;
}
