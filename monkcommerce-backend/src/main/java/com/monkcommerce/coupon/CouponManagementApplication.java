package com.monkcommerce.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Monk Commerce Coupon Management API.
 * <p>
 * This Spring Boot application provides a RESTful backend for managing the
 * creation, updating, retrieval, and application of discount coupons.
 * <ul>
 * <li>Supports multiple coupon types: cart-wise, product-wise, and Buy-X-Get-Y
 * (BxGy).</li>
 * <li>Exposes clean, best-practice RESTful endpoints for coupon
 * operations.</li>
 * <li>Designed for extensibility and future integration with various
 * databases.</li>
 * </ul>
 * 
 * <b>How to run:</b>
 * 
 * <pre>
 *   Use the main() method to launch the application:
 *   java -jar coupon-management-api.jar
 * </pre>
 * 
 * For more details, refer to the README documentation in the project root.
 * 
 * @author Monk Commerce Team
 * @since 2025
 */
@SpringBootApplication
public class CouponManagementApplication {

	/**
	 * Application entry point. Bootstraps the Spring context.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		SpringApplication.run(CouponManagementApplication.class, args);
	}
}
