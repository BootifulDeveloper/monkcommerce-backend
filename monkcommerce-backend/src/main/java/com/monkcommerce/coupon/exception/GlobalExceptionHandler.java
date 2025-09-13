package com.monkcommerce.coupon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Handle CouponNotFoundException
	 */
	@ExceptionHandler(CouponNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCouponNotFoundException(CouponNotFoundException ex, WebRequest request) {

		log.error("Coupon not found: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Coupon Not Found",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle InvalidCouponException
	 */
	@ExceptionHandler(InvalidCouponException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCouponException(InvalidCouponException ex, WebRequest request) {

		log.error("Invalid coupon: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Coupon",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle CouponExpiredException
	 */
	@ExceptionHandler(CouponExpiredException.class)
	public ResponseEntity<ErrorResponse> handleCouponExpiredException(CouponExpiredException ex, WebRequest request) {

		log.error("Coupon expired: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Coupon Expired",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle InsufficientCartValueException
	 */
	@ExceptionHandler(InsufficientCartValueException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientCartValueException(InsufficientCartValueException ex,
			WebRequest request) {

		log.error("Insufficient cart value: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Insufficient Cart Value",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle CouponNotApplicableException
	 */
	@ExceptionHandler(CouponNotApplicableException.class)
	public ResponseEntity<ErrorResponse> handleCouponNotApplicableException(CouponNotApplicableException ex,
			WebRequest request) {

		log.error("Coupon not applicable: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Coupon Not Applicable",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle DuplicateCouponException
	 */
	@ExceptionHandler(DuplicateCouponException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateCouponException(DuplicateCouponException ex,
			WebRequest request) {

		log.error("Duplicate coupon: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), "Duplicate Coupon",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	/**
	 * Handle validation errors from @Valid
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			WebRequest request) {

		log.error("Validation failed: {}", ex.getMessage());

		BindingResult bindingResult = ex.getBindingResult();
		List<ErrorResponse.ValidationError> validationErrors = bindingResult.getFieldErrors().stream()
				.map(fieldError -> new ErrorResponse.ValidationError(fieldError.getField(),
						fieldError.getRejectedValue(), fieldError.getDefaultMessage()))
				.collect(Collectors.toList());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failed",
				"Input validation failed", getPath(request), validationErrors);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle constraint violations
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {

		log.error("Constraint violation: {}", ex.getMessage());

		List<ErrorResponse.ValidationError> validationErrors = ex.getConstraintViolations().stream()
				.map(violation -> new ErrorResponse.ValidationError(violation.getPropertyPath().toString(),
						violation.getInvalidValue(), violation.getMessage()))
				.collect(Collectors.toList());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Constraint Violation",
				"Constraint validation failed", getPath(request), validationErrors);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle method argument type mismatch
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex, WebRequest request) {

		log.error("Method argument type mismatch: {}", ex.getMessage());

		String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", ex.getValue(),
				ex.getName(), ex.getRequiredType().getSimpleName());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Parameter Type",
				message, getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle malformed JSON
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
			WebRequest request) {

		log.error("Malformed JSON request: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Malformed JSON",
				"Request body contains invalid JSON", getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle IllegalArgumentException
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
			WebRequest request) {

		log.error("Illegal argument: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Argument",
				ex.getMessage(), getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle generic exceptions
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {

		log.error("Unexpected error occurred", ex);

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "An unexpected error occurred. Please try again later.", getPath(request));

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Extract path from WebRequest
	 */
	private String getPath(WebRequest request) {
		return request.getDescription(false).replace("uri=", "");
	}
}