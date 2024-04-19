package com.github.vinicius2335.passin.repositories;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
import com.github.vinicius2335.passin.util.creator.CheckInCreator;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Unit testing for check-in repository")
@ActiveProfiles("test")
class CheckInRepositoryTest {
    @Autowired
    private CheckInRepository undertest;
    @Autowired
    private AttendeeRepository attendeeRepository;
    @Autowired
    private EventRepository eventRepository;

    private CheckIn checkIn;
    private Attendee attendee;

    @BeforeEach
    void setUp() {
        Event event = eventRepository.save(EventCreator.mockEvent());
        attendee = attendeeRepository.save(AttendeeCreator.mockAttendee(event));
        checkIn = CheckInCreator.mockCheckIn(attendee);
    }

    @Test
    @DisplayName("save() create new check-in")
    void givenCheckIn_whenSave_thenInsertAndReturnCheckInSaved(){
        // given
        CheckIn newCheckIn = checkIn;
        // when
        CheckIn expected = undertest.save(newCheckIn);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(checkIn);
    }

    private CheckIn initDatabase(){
        return undertest.save(checkIn);
    }

    @Test
    @DisplayName("findByAttendeeId() return Optional Check-In")
    void givenAttendeeId_whenFindByAttendeeId_thenOptionalAttendee() {
        CheckIn checkInSaved = initDatabase();
        // given
        Integer attendeeId = attendee.getId();
        // when
        Optional<CheckIn> expected = undertest.findByAttendeeId(attendeeId);
        // then
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .hasValueSatisfying(
                        checkInTest ->
                                assertThat(checkInTest)
                                        .usingRecursiveComparison()
                                        .isEqualTo(checkInSaved)
                );
    }

    @Test
    @DisplayName("findByAttendeeId() return Optional Empty when not found check-in by attendee id")
    void givenAttendeeId_whenFindByAttendeeId_thenOptionalEmptyAttendee() {
        // given
        Integer attendeeId = attendee.getId();
        // when
        Optional<CheckIn> expected = undertest.findByAttendeeId(attendeeId);
        // then
        assertThat(expected)
                .isEmpty();
    }
}