package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.location.dto.LocationDto;
import ru.practicum.ewm.util.Constants;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateAdminDto {

    private String annotation;

    private Integer category;

    private String description;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;
}
