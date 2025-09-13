package com.monkcommerce.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monkcommerce.coupon.dto.request.CreateCouponDto;
import com.monkcommerce.coupon.dto.request.UpdateCouponDto;
import com.monkcommerce.coupon.dto.response.ApplicableCouponDto;
import com.monkcommerce.coupon.entity.Cart;
import com.monkcommerce.coupon.entity.Coupon;
import com.monkcommerce.coupon.enums.CouponType;
import com.monkcommerce.coupon.exception.CouponNotFoundException;
import com.monkcommerce.coupon.exception.InvalidCouponException;
import com.monkcommerce.coupon.repository.CouponRepository;
import com.monkcommerce.coupon.service.CouponService;
import com.monkcommerce.coupon.service.strategy.CouponStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final Map<CouponType, CouponStrategy> couponStrategies;

	@Override
	public Coupon createCoupon(CreateCouponDto createCouponDto) {
		log.info("Creating coupon of type: {}", createCouponDto.getType());

		Coupon coupon = new Coupon();
		coupon.setType(CouponType.fromString(createCouponDto.getType()));
		coupon.setDetails(convertDetailsToStringMap(createCouponDto.getDetails()));
		coupon.setExpirationDate(createCouponDto.getExpirationDate());
		coupon.setIsActive(true);
		coupon.setCreatedAt(LocalDateTime.now());
		coupon.setUpdatedAt(LocalDateTime.now());

		return couponRepository.save(coupon);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Coupon> getAllCoupons() {
		log.info("Fetching all coupons");
		return couponRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Coupon getCouponById(Long id) {
		log.info("Fetching coupon by id: {}", id);
		return couponRepository.findById(id)
				.orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
	}

	@Override
	public Coupon updateCoupon(Long id, UpdateCouponDto updateCouponDto) {
		log.info("Updating coupon with id: {}", id);

		Coupon existingCoupon = getCouponById(id);

		if (updateCouponDto.getType() != null) {
			existingCoupon.setType(CouponType.fromString(updateCouponDto.getType()));
		}
		if (updateCouponDto.getDetails() != null) {
			existingCoupon.setDetails(convertDetailsToStringMap(updateCouponDto.getDetails()));
		}
		if (updateCouponDto.getExpirationDate() != null) {
			existingCoupon.setExpirationDate(updateCouponDto.getExpirationDate());
		}
		if (updateCouponDto.getIsActive() != null) {
			existingCoupon.setIsActive(updateCouponDto.getIsActive());
		}

		existingCoupon.setUpdatedAt(LocalDateTime.now());

		return couponRepository.save(existingCoupon);
	}

	@Override
	public void deleteCoupon(Long id) {
		log.info("Deleting coupon with id: {}", id);

		if (!couponRepository.existsById(id)) {
			throw new CouponNotFoundException("Coupon not found with id: " + id);
		}

		couponRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ApplicableCouponDto> getApplicableCoupons(Cart cart) {
		log.info("Finding applicable coupons for cart with {} items", cart.getItems().size());

		List<Coupon> activeCoupons = couponRepository.findActiveAndNotExpired(LocalDateTime.now());

		return activeCoupons.stream().filter(coupon -> {
			CouponStrategy strategy = couponStrategies.get(coupon.getType());
			return strategy != null && strategy.isApplicable(cart, coupon);
		}).map(coupon -> {
			CouponStrategy strategy = couponStrategies.get(coupon.getType());
			double discount = strategy.calculateDiscount(cart, coupon);
			return new ApplicableCouponDto(coupon.getId(), coupon.getType().getValue(), discount);
		}).collect(Collectors.toList());
	}

	@Override
	public Cart applyCoupon(Long couponId, Cart cart) {
		log.info("Applying coupon {} to cart", couponId);

		Coupon coupon = couponRepository.findByIdAndIsActiveTrue(couponId)
				.orElseThrow(() -> new CouponNotFoundException("Active coupon not found with id: " + couponId));

		// Check if coupon is expired
		if (coupon.getExpirationDate() != null && coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
			throw new InvalidCouponException("Coupon has expired");
		}

		CouponStrategy strategy = couponStrategies.get(coupon.getType());
		if (strategy == null) {
			throw new InvalidCouponException("Unsupported coupon type: " + coupon.getType());
		}

		if (!strategy.isApplicable(cart, coupon)) {
			throw new InvalidCouponException("Coupon is not applicable to this cart");
		}

		return strategy.applyCoupon(cart, coupon);
	}

	/**
	 * Helper method to convert Object values to String for database storage
	 */
	private Map<String, String> convertDetailsToStringMap(Map<String, Object> details) {
		Map<String, String> stringDetails = new HashMap<>();
		details.forEach((key, value) -> stringDetails.put(key, value.toString()));
		return stringDetails;
	}

	/**
	 * Get active coupons by type - utility method
	 */
	@Transactional(readOnly = true)
	public List<Coupon> getActiveCouponsByType(CouponType type) {
		return couponRepository.findByTypeAndActiveAndNotExpired(type, LocalDateTime.now());
	}

	/**
	 * Get coupons expiring within specified days
	 */
	@Transactional(readOnly = true)
	public List<Coupon> getCouponsExpiringWithin(int days) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime futureDate = now.plusDays(days);
		return couponRepository.findCouponsExpiringWithin(now, futureDate);
	}
}