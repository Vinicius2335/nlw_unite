package com.github.vinicius2335.passin.repositories;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, Integer> {
    List<Attendee> findByEventId(String eventId);

    Optional<Attendee> findByEventIdAndEmail(String eventId, String email);

    Page<Attendee> findByEventIdAndNameContaining(String eventId, String name, Pageable pageable);
}
