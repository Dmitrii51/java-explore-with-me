package ru.practicum.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.service.event.location.dto.LocationDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventNewDto {

    @NotBlank
    @NotEmpty
    private String annotation;

    @NotNull
    @Min(0)
    private Integer category;

    @NotBlank
    @NotEmpty
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    @NotNull
    @Min(0)
    private Integer participantLimit;

    @NotNull
    private Boolean requestModeration;

    @NotBlank
    @NotEmpty
    private String title;
}
