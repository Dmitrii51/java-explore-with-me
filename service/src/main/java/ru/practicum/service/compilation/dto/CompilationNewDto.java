package ru.practicum.service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationNewDto {

    private List<Integer> events = new LinkedList<>();

    private Boolean pinned = false;

    @NotNull
    @NotEmpty
    private String title;
}
