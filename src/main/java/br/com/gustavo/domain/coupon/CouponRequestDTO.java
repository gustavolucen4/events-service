package br.com.gustavo.domain.coupon;

public record CouponRequestDTO(String code, Integer discount, Long valid) {
}
