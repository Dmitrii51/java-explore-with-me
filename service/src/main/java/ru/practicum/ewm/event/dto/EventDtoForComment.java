package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.location.dto.LocationDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.util.Constants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDtoForComment {

    @NotNull
    @Min(0)
    private Integer id;

    @NotBlank
    private String title;

    @NotBlank
    @NotEmpty
    private String annotation;

    @NotNull
    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @NotBlank
    @NotEmpty
    private String description;

    @NotNull
    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    @NotNull
    @Min(0)
    private Integer participantLimit;

    @NotNull
    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    @NotNull
    private EventState state;
}
