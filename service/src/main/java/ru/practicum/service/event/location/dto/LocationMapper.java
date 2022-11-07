package ru.practicum.service.event.location.dto;

import ru.practicum.service.event.location.model.Location;

public class LocationMapper {

    public static Location fromLocationDto(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLatitude(),
                locationDto.getLongitude());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLatitude(),
                location.getLongitude());
    }
}
