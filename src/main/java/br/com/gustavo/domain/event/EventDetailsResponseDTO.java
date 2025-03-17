package br.com.gustavo.domain.event;


import br.com.gustavo.domain.coupon.CouponResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventDetailsResponseDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime date,
        String city,
        String state,
        Boolean remote,
        String eventUrl,
        String imageUtl,
        List<CouponResponseDTO> coupons

) {
}
