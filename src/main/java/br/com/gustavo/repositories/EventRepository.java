package br.com.gustavo.repositories;

import br.com.gustavo.domain.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e WHERE e.date >= :currentDate")
    Page<Event> findUpcomingEvents(@Param("currentDate")Date currentDate, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "LEFT JOIN Address a ON e.id = a.event.id " +
            "WHERE (:title IS NULL OR e.title LIKE %:title%) AND " +
            "(:city IS NULL OR a.city LIKE %:city%) AND " +
            "(:uf IS NULL OR a.uf LIKE %:uf%) AND " +
            "(COALESCE(:startDate, e.date) = e.date OR e.date >= :startDate) AND " +
            "(COALESCE(:endDate, e.date) = e.date OR e.date <= :endDate)")
    Page<Event> findFilteredEvents(@Param("title") String title,
                                   @Param("city") String city,
                                   @Param("uf") String uf,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);
}
