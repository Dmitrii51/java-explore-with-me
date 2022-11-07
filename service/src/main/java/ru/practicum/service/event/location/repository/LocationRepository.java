package ru.practicum.service.event.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.event.location.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    @Query(value = "SELECT l FROM Location l " +
            "WHERE l.latitude = ?1 " +
            "AND l.longitude = ?2")
    Optional<Location> getByLatAndLon(Float latitude, Float longitude);
}
