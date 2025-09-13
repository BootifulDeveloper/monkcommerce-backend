# MonkCommerce Coupon Module 
## 1. Overview
This module handles creation, management, and application of coupons in MonkCommerce.
Supports multiple coupon types: **cart-wise**, **product-wise**, and **BXGY**.
Uses **Strategy Pattern** to flexibly apply coupons.

---

## 2. Architecture Diagram

```
      +----------------+
      |  CouponController  |
      +---------+------+
                |
                v
      +----------------+
      |  CouponService  |
      +---------+------+
        |        |
        |        +-------------------+
        |                            |
        v                            v
+----------------+           +------------------+
| CouponRepository |         |  CouponStrategy  |
+----------------+           +------------------+
                                 |   ^    ^
                    +------------+   |    +------------+
                    |                |                 |
         +----------------+   +----------------+   +----------------+
         | CartWiseStrategy|   | ProductWiseStrategy| | BXGYStrategy  |
         +----------------+   +----------------+   +----------------+
```

**Legend:**

* Controller → Service → Repository (standard flow)
* Service uses **Strategy pattern** to apply coupon logic
* Strategies are pluggable, new types can be added easily

---

## 3. Entities Diagram (ERD)

```
+-----------+          +-----------+          +---------+
|   Coupon  |1        *|   Cart    |1        *| CartItem|
+-----------+          +-----------+          +---------+
| id        |          | items     |<>--------| productId|
| type      |          | totalPrice|          | quantity |
| details   |          | totalDiscount|       | price    |
| isActive  |          | finalPrice |        | totalDiscount |
| expirationDate|                              
+-----------+ 
```

**Notes:**

* A **Cart** has multiple **CartItems**
* Coupon is applied to a Cart via Strategy

---

## 4. Coupon Strategy Flow

```
Client CartDto ---> CouponService ---> Strategy Selector
                                         |
      +-------------------------+--------+---------+
      |                         |                  |
CartWiseStrategy         ProductWiseStrategy      BXGYStrategy
      |                         |                  |
      v                         v                  v
CalculateDiscount          CalculateDiscount   CalculateDiscount
ApplyDiscount              ApplyDiscount     ApplyDiscount
      |                         |                  |
      +-----------+-------------+------------------+
                  v
            Updated Cart with Discounts
```

---

## 5. API Request/Response Flow

```
Client
  |
  | POST /api/v1/coupons
  v
CouponController
  |
  | calls createCoupon(CreateCouponDto)
  v
CouponServiceImpl
  |
  | validates & persists
  v
CouponRepository
  |
  v
DB (coupons table)
```

**Example:** Applying a coupon
```
Client sends CartDto ---> /api/v1/applicable-coupons ---> Controller ---> Service
Service selects applicable coupons using strategies ---> returns List<ApplicableCouponDto>
Client selects coupon ---> /api/v1/apply-coupon/{id} ---> Service applies discount ---> returns updated Cart
```

## 6. Key Takeaways

* **Flexible Design:** Strategy pattern allows new coupon types without modifying service layer
* **Performance Optimized:** Indexed columns and stream-based calculations
* **Validation:** Both DTO-level (Jakarta Validation) and service-level (active, expiration, applicability)
* **Extensible:** Adding new coupon type = implement `CouponStrategy` and register

---


