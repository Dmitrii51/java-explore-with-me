package ru.practicum.ewm.event.location.dto;

import ru.practicum.ewm.event.location.model.Location;

public class LocationMapper {

    public static Location fromLocationDto(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLat(),
                locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLatitude(),
                location.getLongitude());
    }
}
