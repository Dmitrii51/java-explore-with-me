package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;

import javax.transaction.Transactional;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Compilation c SET c.pinned = ?2 " +
            "WHERE c.id = ?1")
    int pinUnpinCompilation(Integer compilationId, Boolean pinned);

    List<Compilation> findAllByPinned(boolean pinned, PageRequest limit);
}
