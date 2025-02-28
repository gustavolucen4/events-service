package br.com.gustavo.repositories;

import br.com.gustavo.domain.coupon.Coupon;
import br.com.gustavo.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
}
