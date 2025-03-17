package br.com.gustavo.service;

import br.com.gustavo.domain.coupon.Coupon;
import br.com.gustavo.domain.coupon.CouponResponseDTO;
import br.com.gustavo.domain.event.Event;
import br.com.gustavo.domain.event.EventDetailsResponseDTO;
import br.com.gustavo.domain.event.EventRequestDTO;
import br.com.gustavo.domain.event.EventResponseDTO;
import br.com.gustavo.repositories.EventRepository;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final EventRepository repository;
    private final AddressService addressService;
    private final CouponService couponService;
    private final AmazonS3 amazonS3;

    public Event createEvent(EventRequestDTO data){
        String imgUrl = null;

        if(data.image() != null)
            imgUrl = this.uploadImage(data.image());

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(Instant.ofEpochMilli(data.date())
                .atZone(ZoneId.systemDefault()) // Usa o fuso horário do sistema
                .toLocalDateTime());
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        newEvent = repository.save(newEvent);

        if (!data.remote()){
            addressService.createAddress(data, newEvent);
        }

        return newEvent;
    }

    private String uploadImage(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try{
            File file = this.convertMultipartToFile(multipartFile);
            amazonS3.putObject(bucketName, fileName, file);
            file.delete();
            return amazonS3.getUrl(bucketName, fileName).toString();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convFile;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = repository.findUpcomingEvents(new Date(), pageable);
        return eventPage.stream().map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())
        ).toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String state, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = repository.findFilteredEvents(title, city, state, startDate, endDate, pageable);
        return eventPage.stream().map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())
        ).toList();
    }

    public EventDetailsResponseDTO getEventById(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found."));

        List<Coupon> coupons = couponService.findCouponByEventId(eventId);

        return new EventDetailsResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl(),
                coupons.stream().map(coupon -> new CouponResponseDTO(coupon.getId(), coupon.getCode(), coupon.getDiscount(), coupon.getValid())).collect(Collectors.toList())
        );
    }
}
