package br.com.gustavo.service;

import br.com.gustavo.domain.coupon.Coupon;
import br.com.gustavo.domain.coupon.CouponRequestDTO;
import br.com.gustavo.domain.event.Event;
import br.com.gustavo.repositories.CouponRepository;
import br.com.gustavo.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repository;
    private final EventRepository eventRepository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO request){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found."));

        Coupon coupon = new Coupon();
        coupon.setCode(request.code());
        coupon.setDiscount(request.discount());
        coupon.setValid(new Date(request.valid()));
        coupon.setEvent(event);

        return repository.save(coupon);
    }
}
