package com.github.vinicius2335.passin.repositories;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
}
