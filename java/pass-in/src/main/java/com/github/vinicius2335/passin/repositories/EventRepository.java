package com.github.vinicius2335.passin.repositories;

import com.github.vinicius2335.passin.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
