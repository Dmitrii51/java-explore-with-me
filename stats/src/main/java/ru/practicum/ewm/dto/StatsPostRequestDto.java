package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsPostRequestDto {

    private Integer id;

    @NotBlank
    @NotEmpty
    private String app;

    @NotBlank
    @NotEmpty
    private String uri;

    @NotBlank
    @NotEmpty
    private String ip;

    @JsonFormat(pattern = Constants.DATE_PATTERN)
    @NotNull
    private LocalDateTime timestamp;
}
