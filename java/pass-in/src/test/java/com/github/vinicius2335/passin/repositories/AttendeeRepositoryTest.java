package com.github.vinicius2335.passin.repositories;

import com.github.javafaker.Faker;
import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AttendeeRepositoryTest {
    @Autowired
    private AttendeeRepository undertest;
    @Autowired
    private EventRepository eventRepository;

    private Event event;

    private Faker faker = new Faker(new Locale("pt-BR"));

    @BeforeEach
    void setUp() {
        event = EventCreator.mockEvent();
        eventRepository.save(event);
    }

    @Test
    void findByEventId() {
        Attendee attendee = new Attendee();
        attendee.setName(faker.name().fullName());
        attendee.setEmail(faker.internet().emailAddress());
        attendee.setCreatedAt(OffsetDateTime.now());
        attendee.setEvent(null);

        undertest.save(attendee);
    }

    @Test
    void findByEventIdAndEmail() {
    }

    @Test
    void findByEventIdAndNameContaining() {
    }
}