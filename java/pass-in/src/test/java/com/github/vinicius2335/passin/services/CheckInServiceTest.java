package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.dto.checkin.CheckInIdResponseDTO;
import com.github.vinicius2335.passin.repositories.CheckInRepository;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
import com.github.vinicius2335.passin.util.creator.CheckInCreator;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Log4j2
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Unit test for check-in service")
class CheckInServiceTest {

    @InjectMocks
    private CheckInService undertest;

    @Mock
    private CheckInRepository mockCheckInRepository;

    private CheckIn checkIn;
    private Attendee attendee;

    @BeforeEach
    void setUp() {
        Event event = EventCreator.mockEvent();
        attendee = AttendeeCreator.mockAttendee(event);
        checkIn = CheckInCreator.mockCheckIn(attendee);

        given(mockCheckInRepository.findByAttendeeId(anyInt()))
                .willReturn(Optional.empty());

        given(mockCheckInRepository.save(any(CheckIn.class)))
                .willReturn(checkIn);
    }

    @Test
    @DisplayName("checkInAttendee() realize attendee check-in")
    void givenAttendee_whenCheckInAttendee_thenRealizeAttendeeCheckInAndReturnResponse() {
        // when
        CheckInIdResponseDTO expected = undertest.checkInAttendee(attendee);

        verify(mockCheckInRepository, times(1))
                .findByAttendeeId(attendee.getId());

        verify(mockCheckInRepository, times(1))
                .save(any(CheckIn.class));

        assertThat(expected)
                .isNotNull()
                .isEqualTo(new CheckInIdResponseDTO(checkIn.getId()));
    }

    @Test
    @DisplayName("checkInAttendee() Throws CheckInAlreadyExistsException when attendee already checked in event")
    void givenAttendee_whenCheckInAttendee_thenThrowsCheckInAlreadyExistsException() {
        // config
        given(mockCheckInRepository.findByAttendeeId(anyInt()))
                .willReturn(Optional.of(checkIn));
        // when
        assertThatThrownBy(() -> undertest.checkInAttendee(attendee))
                .isInstanceOf(CheckInAlreadyExistsException.class)
                        .hasMessage("Attendee already checked in");

        verify(mockCheckInRepository, times(1))
                .findByAttendeeId(attendee.getId());

        verify(mockCheckInRepository, never())
                .save(any(CheckIn.class));
    }

    @Test
    @DisplayName("getOptionalCheckInByAttendeeId() return empty optional if the attendee did not checked in event")
    void givenAttendeeId_whenGetOptionalCheckInByAttendeeId_thenReturnEmptyOptional() {
        // given
        Integer attendeeId = attendee.getId();
        // when
        Optional<CheckIn> expected = undertest.getOptionalCheckInByAttendeeId(attendeeId);
        // then
        verify(mockCheckInRepository, times(1))
                .findByAttendeeId(attendeeId);

        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("getOptionalCheckInByAttendeeId() return optional of attendee if already checked in event")
    void givenAttendeeId_whenGetOptionalCheckInByAttendeeId_thenReturnOptionalAttendee() {
        // config
        given(mockCheckInRepository.findByAttendeeId(anyInt()))
                .willReturn(Optional.of(checkIn));
        // given
        Integer attendeeId = attendee.getId();
        // when
        Optional<CheckIn> expected = undertest.getOptionalCheckInByAttendeeId(attendeeId);
        // then
        verify(mockCheckInRepository, times(1))
                .findByAttendeeId(attendeeId);

        assertThat(expected)
                .isPresent()
                .hasValueSatisfying(
                        checkIn1 -> assertThat(checkIn1)
                                .isNotNull()
                                .usingRecursiveComparison()
                                .isEqualTo(checkIn)
                );
    }
}