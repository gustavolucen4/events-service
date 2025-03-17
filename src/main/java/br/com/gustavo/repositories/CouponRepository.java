package br.com.gustavo.repositories;

import br.com.gustavo.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    List<Coupon> findCouponsByEvent_Id(UUID eventId);
}
