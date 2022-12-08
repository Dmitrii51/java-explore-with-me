package ru.practicum.ewm.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageBuilder {

    public Pageable getPage(int from, int size, String sort, Sort.Direction direction) {
        Sort sortBy = Sort.by(direction, sort);
        return PageRequest.of((from / size), size, sortBy);
    }
}
