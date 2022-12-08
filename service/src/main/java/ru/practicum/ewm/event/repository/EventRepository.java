package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByInitiatorId(Integer initiatorId, Pageable page);

    @Query(value = "SELECT e FROM Event e " +
            "WHERE (COALESCE(:userIds, null) IS NULL OR e.initiator.id IN :userIds) " +
            "AND (COALESCE(:states, null) IS NULL OR e.state IN :states) " +
            "AND (COALESCE(:categoryIds, null) IS NULL OR e.category.id IN :categoryIds) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> findEventsByAdmin(
            @Param("userIds") List<Integer> userIds, @Param("states") List<EventState> states,
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);


    @Query(value = "SELECT e FROM Event e " +
            "WHERE ((:text IS NULL OR lower(e.annotation) LIKE concat('%', :text, '%')) " +
            "OR (:text IS NULL OR lower(e.description) LIKE concat('%', :text, '%'))) " +
            "AND (coalesce(:categories, null) IS NULL OR e.category.id IN :categories) " +
            "AND (coalesce(:paid, null) IS NULL OR e.paid = :paid) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND e.state = 'PUBLISHED'")
    List<Event> findPublishedEvents(
            @Param("text") String text, @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);
}
