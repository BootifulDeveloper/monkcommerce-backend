
# MonkCommerce Coupon Module

## 1. Module Overview

The MonkCommerce Coupon Module is an advanced backend service engineered to power dynamic discounting in modern e-commerce platforms. It enables creation, storage, and flexible application of a wide range of promotional coupons—including cart-wise, product-wise, and Buy-X-Get-Y (BXGY) offers—via RESTful APIs.

Strategic Value:
Boosts customer engagement and sales through targeted, rule-based discounts.
Reduces operational overhead with a unified, automated coupon logic engine.
Supports rapid marketing experimentation and campaign launches.

Key Features:

Extensible Design: Leveraging the Strategy Pattern, each coupon type is encapsulated in its own “strategy,” making the addition of new coupon logic quick, testable, and low risk.
Robust API: Exposes endpoints for the creation, retrieval, updating, deletion, and application of coupons, with validation and error handling throughout.
Dynamic Applicability: Determines and applies relevant discounts automatically, handling edge cases (e.g., repetition limits, product groupings) and ensuring only eligible coupons are presented and enforced.
Future-Proof Schema: Coupon details are stored in a flexible format, making schema migrations and feature expansion seamless.
Maintainable & Scalable: Follows clean code principles, strong separation of concerns, and layered architecture for sustainable long-term maintenance.

Business Impact:

Empowers non-technical teams (marketing/product) to run sophisticated discount campaigns with minimal developer intervention.
Improves shopping experience by ensuring discounts are applied correctly and transparently.
Designed for integration into high-volume, real-time commerce backends with further expansion into usage analytics, personalized promotions, and advanced loyalty features.


## 2. Architecture Highlights

- **Layered Structure:** Controller → Service → Repository.
- **Strategy Pattern:** All coupon logic is encapsulated in separate strategies (CartWise, ProductWise, BXGY, etc.), facilitating future expansion and clean code separation.
- **Extensible Entities:** Coupon details are stored dynamically (e.g., as JSON), enabling new types without DB changes.
- **Validation & Error Handling:** Rigorous input checks and centralized error responses.

Architecture Diagram:
```
+----------------+      +------------------+      +-------------------+
| Controller     +----->| Service          +----->| Repository        |
+----------------+      +------------------+      +-------------------+
                                     |
                                     v
                           +------------------+
                           | Strategy Pattern |
                           +------------------+
                     | CartWise | ProductWise | BXGY | Future Types |
```

***

## 3. Entity Relationships

```
+-----------+           +-----------+           +----------+
| Coupon    |1        * | Cart      |1        * | CartItem |
+-----------+           +-----------+           +----------+
| id        |           | items     |<>-------->| productId|
| type      |           | discount  |           | quantity |
| details   |           | finalPrice|           | price    |
| isActive  |                                       
| expiration|  
+-----------+ 
```
*Each cart can have many cart items; coupons are applied to carts and calculated via their strategy logic.*

***

## 4. Coupon Handling Flow

1. **Create coupon:**  
   - `POST /api/v1/coupons`
2. **Check applicable coupons:**  
   - `POST /api/v1/applicable-coupons` (submit cart DTO; receive matching coupons)
3. **Apply coupon:**  
   - `POST /api/v1/apply-coupon/{id}` (receive updated cart with discounts)

Coupon strategies are selected dynamically by type and invoked for applicability and calculation.

***

## 5. Use Cases

### Implemented Cases

- **Cart-wise Coupons:**  
  Percentage or fixed discount if cart threshold is met.
- **Product-wise Coupons:**  
  Discount applied only to target product(s) in cart.
- **BXGY Coupons:**  
  E.g., Buy 2 from [X, Y, Z], get 1 from [A, B, C] free, supporting repetition limits.
- **Coupon activation, expiry handling, and basic error validation.**

### Unimplemented / Deferred Cases

- **Stacking multiple coupons:**  
  *Requires complex business logic around priorities and exclusions.*
- **User/global limit enforcement:**  
  *Needs user/session tracking and audit trail.*
- **Advanced applicability constraints:**  
  Time/date ranges, product category filters, membership restrictions, cart combinations.
- **Usage analytics and reporting.**
- **Distributed caching and high volume optimizations.**

### Example Payloads

```json
// Cart-wise Coupon
{
  "type": "cart-wise",
  "details": { "threshold": 100, "discount": 10 }
}

// Product-wise Coupon
{
  "type": "product-wise",
  "details": { "product_id": 1, "discount": 20 }
}

// BXGY Coupon
{
  "type": "bxgy",
  "details": { 
    "buy_products": [{ "product_id": 1, "quantity": 3 }, { "product_id": 2, "quantity": 3 }],
    "get_products": [{ "product_id": 3, "quantity": 1 }],
    "repetition_limit": 2
  }
}
```

***

## 6. Assumptions

- Cart DTOs contain product IDs, quantities, and prices.
- Only one coupon is applied per cart.
- "Get" products in BXGY are always given for free.
- Coupon logic is modular, and new types require just a new strategy class.
- Currency and decimal precision is uniform.
- No per-user authentication or session tracking (future area).

***

## 7. Limitations

- Single coupon application per checkout; stacking/combinatorial not supported.
- No integration with real user/accounts, product catalogs, or inventory services.
- No multi-language/i18n support.
- No explicit coupon usage history/logging.

***

## 8. Key Takeaways

- **Strategy pattern** ensures flexible, future-proof coupon type expansion.
- **Validation** at DTO and strategy levels for correct application.
- **Clean, maintainable codebase** enables rapid growth and refactoring.
- All system assumptions and limits are transparent for easy auditing.
- Clear separation for implemented, unimplemented, limitations, and suggestions for improvement.

***
