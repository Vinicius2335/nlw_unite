package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeDetail;
import com.github.vinicius2335.passin.dto.attendee.PageAttendeesResponse;
import com.github.vinicius2335.passin.dto.checkin.CheckInIdResponseDTO;
import com.github.vinicius2335.passin.repositories.AttendeeRepository;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
import com.github.vinicius2335.passin.util.creator.CheckInCreator;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit testing for attendee service")
@ActiveProfiles("test")
@MockitoSettings(strictness = Strictness.LENIENT)
@Log4j2
class AttendeeServiceTest {
    @InjectMocks
    private AttendeeService undertest;

    @Mock
    private AttendeeRepository mockAttendeeRepository;
    @Mock
    private CheckInService mockCheckInService;

    private Attendee attendee;
    private final Event event = EventCreator.mockEvent();
    private CheckIn checkIn;

    @BeforeEach
    void setUp() {
        attendee = AttendeeCreator.mockAttendee(event);
        checkIn = CheckInCreator.mockCheckIn(attendee);
        PageImpl<Attendee> attendeePage = new PageImpl<>(List.of(attendee));

        when(mockAttendeeRepository.findById(anyInt()))
                .thenReturn(Optional.of(attendee));

        when(mockAttendeeRepository.findByEventId(anyString()))
                .thenReturn(List.of(attendee));

        when(mockAttendeeRepository.findByEventIdAndNameContaining(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(attendeePage);

        when(mockCheckInService.getOptionalCheckInByAttendeeId(anyInt()))
                .thenReturn(Optional.of(checkIn));

        when(mockAttendeeRepository.save(any(Attendee.class)))
                .thenReturn(attendee);

        when(mockAttendeeRepository.findByEventIdAndEmail(anyString(), anyString()))
                .thenReturn(Optional.empty());

        when(mockCheckInService.checkInAttendee(any(Attendee.class)))
                .thenReturn(new CheckInIdResponseDTO(checkIn.getId()));
    }

    @Test
    @DisplayName("getAttendeeByIdOrThrowsException() return attendee found by id")
    void givenAttendeeId_whenGetAttendeeByIdOrThrowsException_thenReturnAttendee() {
        // given
        Integer attendeeId = attendee.getId();
        // when
        Attendee expected = undertest.getAttendeeByIdOrThrowsException(attendeeId);
        // then
        verify(mockAttendeeRepository, times(1))
                .findById(attendeeId);

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(attendee);
    }

    @Test
    @DisplayName("getAttendeeByIdOrThrowsException() throws AttendeeNotFoundException when attendee not found by id")
    void givenAttendeeId_whenGetAttendeeByIdOrThrowsException_thenThrowsAttendeeNotFoundException() {
        // config
        when(mockAttendeeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        // given
        Integer attendeeId = attendee.getId();
        // when
        assertThatThrownBy(() -> undertest.getAttendeeByIdOrThrowsException(attendeeId))
                .isInstanceOf(AttendeeNotFoundException.class)
                .hasMessageContaining("Attendee not found with attendeeId:");
        // then
        verify(mockAttendeeRepository, times(1))
                .findById(attendeeId);
    }

    @Test
    @DisplayName("getAllAttendeesFromEvent() returns a list of attenedees who are registered for a given event")
    void givenEventId_whenGetAllAttendeesFromEvent_thenReturnListAttendees() {
        // given
        String eventId = event.getId();
        // when
        List<Attendee> expected = undertest.getAllAttendeesFromEvent(eventId);
        // then
        verify(mockAttendeeRepository, times(1))
                .findByEventId(eventId);

        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(attendee);
    }

    @Test
    @DisplayName("getEventsAttendee() return an page of attendee response")
    void givenEventIdAndAttendeeName_whenGetEventsAttendee_thenReturnPageAttendeesResponse() {
        AttendeeDetail attendeeDetail = AttendeeCreator.mockAttendeeDetail(attendee, checkIn);
        // given
        String eventId = event.getId();
        String name = attendee.getName();
        PageRequest pageable = PageRequest.of(0, 10);
        // when
        PageAttendeesResponse expected = undertest.getEventsAttendee(eventId, name, pageable);
        log.info(expected);
        // then
        verify(mockAttendeeRepository, times(1))
                .findByEventIdAndNameContaining(eventId, name, pageable);
        verify(mockCheckInService, times(1))
                .getOptionalCheckInByAttendeeId(attendee.getId());

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(expected).as("Not Null").isNotNull();
        assertions.assertThat(expected.totalItens()).as("Total Itens").isEqualTo(1);
        assertions.assertThat(expected.attendees()).as("List of Attendees")
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(attendeeDetail);
        assertions.assertAll();
    }

    @Test
    @DisplayName("registerAttendee() register attendee in database")
    void givenAttendee_whenRegisterAttendee_thenReturnAndInsertNewAttendee() {
        // given
        // when
        Attendee expected = undertest.registerAttendee(attendee);
        log.info(expected);
        // then
        verify(mockAttendeeRepository, times(1)).save(attendee);

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(attendee);
    }

    @Test
    @DisplayName("verifyAttendeeSubscription() check if the attendee is already registered for the event")
    void givenEmailAndEventID_whenVerifyAttendeeSubscription() {
        // given
        String email = attendee.getEmail();
        String eventId = attendee.getEvent().getId();
        // when
        assertThatCode(() -> undertest.verifyAttendeeSubscription(email, eventId))
                .doesNotThrowAnyException();
        // then
        verify(mockAttendeeRepository, times(1))
                .findByEventIdAndEmail(eventId, email);
    }

    @Test
    @DisplayName("verifyAttendeeSubscription() Throws AttendeeAlreadyExistException when attendee already registered in the event")
    void givenEmailAndEventID_whenVerifyAttendeeSubscription_thenThrowsAttendeeAlreadyExistExceptio() {
        // config
        when(mockAttendeeRepository.findByEventIdAndEmail(anyString(), anyString()))
                .thenReturn(Optional.of(attendee));
        // given
        String email = attendee.getEmail();
        String eventId = attendee.getEvent().getId();
        // when
        assertThatThrownBy(
                () -> undertest.verifyAttendeeSubscription(email, eventId))
                .isInstanceOf(AttendeeAlreadyExistException.class)
                .hasMessage("Attendee is already registered");
        // then
        verify(mockAttendeeRepository, times(1))
                .findByEventIdAndEmail(eventId, email);
    }

    @Test
    @DisplayName("getAttendeeBadge() return the badge of attendee")
    void givenAttendeeId_whenGetAttendeeBadge_thenReturnBadgeOfAttendee() {
        AttendeeBadgeResponseDTO attendeeBadgeResponseDTO = AttendeeCreator.mockAttendeeBadgeResponseDTO(attendee);
        // given
        Integer attendeeId = attendee.getId();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        // when
        AttendeeBadgeResponseDTO expected = undertest.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        log.info(expected);
        // then
        verify(mockAttendeeRepository, times(1))
                .findById(attendeeId);

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(attendeeBadgeResponseDTO);
    }

    @Test
    @DisplayName("getAttendeeBadge() Throws AttendeeNotFoundException when attendee not found by id")
    void givenAttendeeId_whenGetAttendeeBadge_thenThrowsAttendeeNotFoundException() {
        // config
        when(mockAttendeeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        // given
        Integer attendeeId = attendee.getId();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        // when
        assertThatThrownBy(() -> undertest.getAttendeeBadge(attendeeId, uriComponentsBuilder))
                .isInstanceOf(AttendeeNotFoundException.class)
                        .hasMessageContaining("Attendee not found with attendeeId");
        // then
        verify(mockAttendeeRepository, times(1))
                .findById(attendeeId);
    }

    @Test
    @DisplayName("registerCheckInAttendee() checks the attendee into the event ")
    void giivenAttendeeId_whenRegisterCheckInAttendee_thenRegisterAttendeeIntoEvent() {
        // given
        Integer attendeeId = attendee.getId();
        // when
        CheckInIdResponseDTO expected = undertest.registerCheckInAttendee(attendeeId);
        // then
        verify(mockAttendeeRepository, times(1))
                .findById(attendeeId);

        verify(mockCheckInService, times(1))
                .checkInAttendee(attendee);

        assertThat(expected.checkInId())
                .isNotNull()
                .isEqualTo(checkIn.getId());
    }

    @Test
    @DisplayName("registerCheckInAttendee() Throws AttendeeNotFoundException when attendee not found by id")
    void giivenAttendeeId_whenRegisterCheckInAttendee_thenThrowsAttendeeNotFoundException() {
        // config
        when(mockAttendeeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        // given
        Integer attendeeId = attendee.getId();
        // when
        assertThatThrownBy(() -> undertest.registerCheckInAttendee(attendeeId))
                .isInstanceOf(AttendeeNotFoundException.class)
                        .hasMessageContaining("Attendee not found with attendeeId");
        // then
        verify(mockAttendeeRepository, times(1))
                .findById(attendeeId);

        verify(mockCheckInService, never())
                .checkInAttendee(attendee);
    }
}