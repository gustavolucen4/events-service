package br.com.gustavo.controller;

import br.com.gustavo.domain.coupon.Coupon;
import br.com.gustavo.domain.coupon.CouponRequestDTO;
import br.com.gustavo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO request){
        Coupon coupon = couponService.addCouponToEvent(eventId, request);
        return ResponseEntity.ok(coupon);
    }
}
