package ru.practicum.service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.request.model.Request;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(value = "SELECT r FROM Request r " +
            "WHERE r.requester.id = ?1 " +
            "AND r.event.id = ?2")
    Optional<Request> getUserRequest(Integer requesterId, Integer eventId);

    @Query(value = "SELECT r FROM Request r " +
            "WHERE r.event.initiator.id = ?1 " +
            "AND r.event.id = ?2 " +
            "AND r.id = ?3")
    Optional<Request> getUserRequest(Integer initiatorId, Integer eventId, Integer requestId);

    @Query(value = "SELECT r FROM Request r " +
            "WHERE r.requester.id = ?1 " +
            "AND r.id = ?2")
    Optional<Request> getUserRequestById(Integer requesterId, Integer requestId);

    @Query("SELECT count(r) FROM Request r " +
            "WHERE r.event.id = ?1 " +
            "AND r.status = 'CONFIRMED'")
    Integer getConfirmedRequests(Integer eventId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Request r SET r.status = 'CONFIRMED' " +
            "WHERE r.id = ?1")
    void confirmRequest(Integer reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Request r SET r.status = 'REJECTED' " +
            "WHERE r.status <> 'CONFIRMED' " +
            "AND r.event.id = ?1")
    void rejectNotConfirmedRequest(Integer eventId);

    List<Request> findAllByRequesterId(Integer requesterId);

    @Query(value = "SELECT r FROM Request r " +
            "WHERE r.event.initiator.id = ?1 " +
            "AND r.event.id = ?2")
    List<Request> getUserEventRequests(Integer initiatorId, Integer eventId);
}
