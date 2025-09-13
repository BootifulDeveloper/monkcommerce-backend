package com.monkcommerce.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

/**
 * Implementation of {@link CouponService} that manages coupon creation,
 * retrieval, update, deletion, and application.
 * <p>
 * Business logic for applying coupons is delegated to different
 * {@link CouponStrategy} implementations based on {@link CouponType}.
 * </p>
 */
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

        Optional.ofNullable(updateCouponDto.getType())
                .ifPresent(type -> existingCoupon.setType(CouponType.fromString(type)));

        Optional.ofNullable(updateCouponDto.getDetails())
                .ifPresent(details -> existingCoupon.setDetails(convertDetailsToStringMap(details)));

        Optional.ofNullable(updateCouponDto.getExpirationDate())
                .ifPresent(existingCoupon::setExpirationDate);

        Optional.ofNullable(updateCouponDto.getIsActive())
                .ifPresent(existingCoupon::setIsActive);

        existingCoupon.setUpdatedAt(LocalDateTime.now());

        return couponRepository.save(existingCoupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        log.info("Deleting coupon with id: {}", id);

        couponRepository.findById(id)
                .ifPresentOrElse(
                        couponRepository::delete,
                        () -> { throw new CouponNotFoundException("Coupon not found with id: " + id); }
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicableCouponDto> getApplicableCoupons(Cart cart) {
        log.info("Finding applicable coupons for cart with {} items", cart.getItems().size());

        return couponRepository.findActiveAndNotExpired(LocalDateTime.now())
                .stream()
                .map(coupon -> {
                    CouponStrategy strategy = couponStrategies.get(coupon.getType());
                    if (strategy != null && strategy.isApplicable(cart, coupon)) {
                        double discount = strategy.calculateDiscount(cart, coupon);
                        return new ApplicableCouponDto(coupon.getId(), coupon.getType().getValue(), discount);
                    }
                    return null;
                })
                .filter(c -> c != null)
                .collect(Collectors.toList());
    }

    @Override
    public Cart applyCoupon(Long couponId, Cart cart) {
        log.info("Applying coupon {} to cart", couponId);

        Coupon coupon = couponRepository.findByIdAndIsActiveTrue(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Active coupon not found with id: " + couponId));

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
     * Converts coupon details to a string-based map for persistence.
     */
    private Map<String, String> convertDetailsToStringMap(Map<String, Object> details) {
        return details.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coupon> getActiveCouponsByType(CouponType type) {
        return couponRepository.findByTypeAndActiveAndNotExpired(type, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coupon> getCouponsExpiringWithin(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);
        return couponRepository.findCouponsExpiringWithin(now, futureDate);
    }
}
