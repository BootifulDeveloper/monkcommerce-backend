package com.monkcommerce.coupon.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private int status;
	private String error;
	private String message;
	private String path;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;

	private List<ValidationError> validationErrors;

	// Constructor for general errors
	public ErrorResponse(int status, String error, String message, String path) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}

	// Constructor for validation errors
	public ErrorResponse(int status, String error, String message, String path,
			List<ValidationError> validationErrors) {
		this(status, error, message, path);
		this.validationErrors = validationErrors;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ValidationError {
		private String field;
		private Object rejectedValue;
		private String message;
	}
}
