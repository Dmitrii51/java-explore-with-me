package ru.practicum.ewm.event.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull
    @Min(-90)
    @Max(90)
    private Float lat;

    @NotNull
    @Min(-180)
    @Max(180)
    private Float lon;
}

