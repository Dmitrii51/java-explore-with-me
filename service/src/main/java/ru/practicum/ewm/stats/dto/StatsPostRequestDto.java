package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsPostRequestDto {

    private Integer id;

    private String app;

    private String uri;

    private String ip;

    @JsonFormat(pattern = Constants.DATE_TIME_PATTERN)
    private String timestamp;
}
