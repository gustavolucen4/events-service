package br.com.gustavo.domain.event;


import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(UUID id, String title, String description, LocalDateTime date, String city, String state, Boolean remote, String eventUrl, String imageUtl) {
}
