package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.util.Constants;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Integer id;

    private Integer event;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private LocalDateTime created;

    private Integer requester;

    private RequestStatus status;
}
