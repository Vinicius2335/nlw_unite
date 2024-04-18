package com.github.vinicius2335.passin.repositories;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AttendeeRepositoryTest {
    @Autowired
    private AttendeeRepository undertest;
    @Autowired
    private EventRepository eventRepository;

    private Attendee attendee;
    private Event event;

    @BeforeEach
    void setUp() {
        event = EventCreator.mockEvent();
        eventRepository.save(event);
        attendee = AttendeeCreator.mockAttendee(event);
    }

    @Test
    @DisplayName("save() insert new attendee")
    void giveAttendee_whenSave_thenInsertAnNewAttendee(){
        // given
        Attendee newAttendee = attendee;
        // when
        Attendee expected = undertest.save(newAttendee);
        //then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt")
                .isEqualTo(newAttendee);
    }

    private Attendee initDataBase(){
        return undertest.save(attendee);
    }

    @Test
    @DisplayName("findByEventId() return all attendee related to event id")
    void givenEventId_whenFindByEventId_thenReturnListOfAttendees() {
        Attendee attendeeSaved = initDataBase();
        // given
        String eventId = attendeeSaved.getEvent().getId();
        // when
        List<Attendee> expected = undertest.findByEventId(eventId);
        // then
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(attendeeSaved);
    }

    @Test
    @DisplayName("findByEventId() return empty list when event dont have any attendee registred")
    void givenEventId_whenFindByEventId_thenReturnEmptyListOfAttendees() {
        // given
        String eventId = event.getId();
        // when
        List<Attendee> expected = undertest.findByEventId(eventId);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("findByEventIdAndEmail() return Optional attendee related to event id and attendee email")
    void givenEventIdAndEmail_whenFindByEventIdAndEmail_thenReturnOptionalAttendee() {
        Attendee attendeeSaved = initDataBase();
        // given
        String eventId = attendeeSaved.getEvent().getId();
        String email = attendeeSaved.getEmail();
        // when
        Optional<Attendee> expected = undertest.findByEventIdAndEmail(eventId, email);
        // then
        assertThat(expected)
                .isNotNull()
                .isPresent()
                .hasValueSatisfying(attendee1 ->
                        assertThat(attendee1)
                        .usingRecursiveComparison()
                        .isEqualTo(attendeeSaved));
                //.contains(attendeeSaved); tambem funcionou
    }

    @Test
    @DisplayName("findByEventIdAndEmail() return empty Optional when attendee not found by event id and email")
    void givenEventIdAndEmail_whenFindByEventIdAndEmail_thenReturnEmptyOptional() {
        // given
        String eventId = event.getId();
        String email = attendee.getEmail();
        // when
        Optional<Attendee> expected = undertest.findByEventIdAndEmail(eventId, email);
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("findByEventIdAndNameContaining() return page of attendee related event id and contains name")
    void givenNameAndEventId_whenFindByEventIdAndNameContaining_thenReturnPageAttendee() {
        Attendee attendeeSaved = initDataBase();
        // given
        String eventId = attendeeSaved.getEvent().getId();
        String name = attendeeSaved.getName();
        PageRequest pageable = PageRequest.of(0, 1);
        // when
        Page<Attendee> expected = undertest.findByEventIdAndNameContaining(
                eventId,
                name,
                pageable
        );
        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(expected)
                .as("Is null?")
                .isNotNull();
        softly.assertThat(expected.getContent())
                .as("Has size 1?")
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(attendeeSaved);
        softly.assertAll();
    }

    @Test
    @DisplayName("findByEventIdAndNameContaining() return page of attendee with empty content")
    void givenNameAndEventId_whenFindByEventIdAndNameContaining_thenReturnPageWithEmptyContent() {
        // given
        String eventId = event.getId();
        String name = attendee.getName();
        PageRequest pageable = PageRequest.of(0, 1);
        // when
        Page<Attendee> expected = undertest.findByEventIdAndNameContaining(
                eventId,
                name,
                pageable
        );
        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(expected)
                .as("Is null?")
                .isNotNull();
        softly.assertThat(expected.getContent())
                .as("Have content?")
                        .isEmpty();
        softly.assertAll();
    }
}