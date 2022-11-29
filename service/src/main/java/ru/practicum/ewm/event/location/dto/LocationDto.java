package ru.practicum.ewm.event.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotNull
    @PositiveOrZero
    private Float latitude;

    @NotNull
    @PositiveOrZero
    private Float longitude;
}

