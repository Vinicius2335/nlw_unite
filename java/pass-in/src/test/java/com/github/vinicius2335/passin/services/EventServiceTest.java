package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.domain.event.exceptions.EventFullException;
import com.github.vinicius2335.passin.domain.event.exceptions.EventNotFoundException;
import com.github.vinicius2335.passin.dto.attendee.AttendeeIdResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventIdResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventListResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;
import com.github.vinicius2335.passin.repositories.EventRepository;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit testing for event service")
@ActiveProfiles("test")
@MockitoSettings(strictness = Strictness.LENIENT)
@Log4j2
class EventServiceTest {
    @InjectMocks
    private EventService undertest;

    @Mock
    private EventRepository mockEventRepository;
    @Mock
    private AttendeeService mockAttendeeService;

    private Event event;
    private Attendee attendee;
    private final String messageNotFoundException = "Event not found with ID:";

    @BeforeEach
    void setUp() {
        event = EventCreator.mockEvent();
        attendee = AttendeeCreator.mockAttendee(event);

        given(mockEventRepository.findAll())
                .willReturn(List.of(event));

        given(mockEventRepository.findById(anyString()))
                .willReturn(Optional.of(event));

        given(mockAttendeeService.getAllAttendeesFromEvent(anyString()))
                .willReturn(List.of(attendee));

        given(mockEventRepository.save(any(Event.class)))
                .willReturn(event);

        doNothing().when(mockAttendeeService)
                .verifyAttendeeSubscription(anyString(), anyString());

        given(mockAttendeeService.registerAttendee(any(Attendee.class)))
                .willReturn(attendee);
    }

    @Test
    @DisplayName("getAllEvent() return response with list of events")
    void whenGetAllEvent_thenReturnResponseWithListOfEvents() {
        EventListResponseDTO eventListResponseDTO = EventCreator.mockEventListResponseDTO(event);
        // when
        EventListResponseDTO expected = undertest.getAllEvent();
        // then
        verify(mockEventRepository, times(1))
                .findAll();

        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, times(1))
                .getAllAttendeesFromEvent(event.getId());

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(eventListResponseDTO);
    }

    @Test
    @DisplayName("getAllEvent() Throws EventNotFoundException when event not found by id")
    void whenGetAllEvent_thenThrowsEventNotFoundException() {
        // config
        given(mockEventRepository.findById(anyString()))
                .willReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> undertest.getAllEvent())
                .isInstanceOf(EventNotFoundException.class)
                        .hasMessageContaining(messageNotFoundException);
        // then
        verify(mockEventRepository, times(1))
                .findAll();

        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, never())
                .getAllAttendeesFromEvent(event.getId());
    }

    @Test
    @DisplayName("getAllEvent() return empty event list response when findAll return empty list")
    void whenGetAllEvent_thenEventFindAllReturnEmptyList() {
        // config
        given(mockEventRepository.findAll())
                .willReturn(List.of());
        // when
        assertThatCode(() -> undertest.getAllEvent())
                .doesNotThrowAnyException();
        // then
        verify(mockEventRepository, times(1))
                .findAll();

        verify(mockEventRepository, never())
                .findById(event.getId());

        verify(mockAttendeeService, never())
                .getAllAttendeesFromEvent(event.getId());
    }

    @Test
    @DisplayName("getEventByIdOrThrowsException() return event by id")
    void givenId_whenGetEventByIdOrThrowsException_thenReturnEvent() {
        // given
        String eventId = event.getId();
        // when
        Event expected = undertest.getEventByIdOrThrowsException(eventId);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(event);
    }

    @Test
    @DisplayName("getEventByIdOrThrowsException() Throws EventNotFoundException when event not found by id")
    void givenId_whenGetEventByIdOrThrowsException_thenThrowsEventNotFoundException() {
        // config
        given(mockEventRepository.findById(anyString()))
                .willReturn(Optional.empty());
        // given
        String eventId = event.getId();
        // when
        assertThatThrownBy(() -> undertest.getEventByIdOrThrowsException(eventId))
                .isInstanceOf(EventNotFoundException.class)
                        .hasMessageContaining(messageNotFoundException);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());
    }

    @Test
    @DisplayName("getEventDetail() return an response with event details")
    void givenEventId_whenGetEventDetail_thenReturnEventDetailsResponse() {
        EventResponseDTO eventResponseDTO = EventCreator.mockEventResponseDTO(event);
        // given
        String eventId = event.getId();
        // when
        EventResponseDTO expected = undertest.getEventDetail(eventId);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, times(1))
                .getAllAttendeesFromEvent(event.getId());

        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(eventResponseDTO);
    }

    @Test
    @DisplayName("getEventDetail() Throws EventNotFoundException when event not found by id")
    void givenEventId_whenGetEventDetail_thenthenThrowsEventNotFoundException() {
        // config
        given(mockEventRepository.findById(anyString()))
                .willReturn(Optional.empty());
        // given
        String eventId = event.getId();
        // when
        assertThatThrownBy(() -> undertest.getEventDetail(eventId))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessageContaining(messageNotFoundException);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, never())
                .getAllAttendeesFromEvent(event.getId());
    }

    @Test
    @DisplayName("createEvent() insert new event in database")
    void givenEventRequest_whenCreateEvent_thenInsertEventAndReturnEventResponseDTO() {
        // given
        EventRequestDTO request = EventCreator.mockEventRequest(event);
        // when
        EventIdResponseDTO expected = undertest.createEvent(request);
        log.info(expected);
        // then
        verify(mockEventRepository, times(1))
                .save(any(Event.class));

        assertThat(expected)
                .isNotNull()
                .isEqualTo(new EventIdResponseDTO(event.getId()));
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() register an attendee in event")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenRegisterAttendeeInEvendAndReturnYourId() {
        // given
        String eventId = event.getId();
        AttendeeRequestDTO request = AttendeeCreator.mockAttendeeRequestDTO(attendee);
        // when
        AttendeeIdResponseDTO expected = undertest.registerAttendeeOnEvent(eventId, request);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, times(1))
                .verifyAttendeeSubscription(request.email(), eventId);

        verify(mockAttendeeService, times(1))
                .getAllAttendeesFromEvent(eventId);

        verify(mockAttendeeService, times(1))
                .registerAttendee(any(Attendee.class));

        assertThat(expected)
                .isNotNull()
                .isEqualTo(new AttendeeIdResponseDTO(attendee.getId()));
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() Throws EventNotFoundException when event not found by id")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenThrowsEventNotFoundException() {
        // config
        given(mockEventRepository.findById(anyString()))
                .willReturn(Optional.empty());
        // given
        String eventId = event.getId();
        AttendeeRequestDTO request = AttendeeCreator.mockAttendeeRequestDTO(attendee);
        // when
        assertThatThrownBy(() -> undertest.registerAttendeeOnEvent(eventId, request))
                .isInstanceOf(EventNotFoundException.class)
                        .hasMessageContaining(messageNotFoundException);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, never())
                .verifyAttendeeSubscription(request.email(), eventId);

        verify(mockAttendeeService, never())
                .getAllAttendeesFromEvent(eventId);

        verify(mockAttendeeService, never())
                .registerAttendee(any(Attendee.class));
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() Throws AttendeeAlreadyExistException when attendee already registered in event")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenThrowsAttendeeAlreadyExistException() {
        // config
        doThrow(AttendeeAlreadyExistException.class).when(mockAttendeeService)
                .verifyAttendeeSubscription(anyString(), anyString());
        // given
        String eventId = event.getId();
        AttendeeRequestDTO request = AttendeeCreator.mockAttendeeRequestDTO(attendee);
        // when
        assertThatThrownBy(() -> undertest.registerAttendeeOnEvent(eventId, request))
                .isInstanceOf(AttendeeAlreadyExistException.class);
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, times(1))
                .verifyAttendeeSubscription(request.email(), eventId);

        verify(mockAttendeeService, never())
                .getAllAttendeesFromEvent(eventId);

        verify(mockAttendeeService, never())
                .registerAttendee(any(Attendee.class));
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() Throws EventFullException when the event is already full")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenThrowsEventFullException() {
        // config
        event.setMaximunAttendees(1);
        given(mockEventRepository.findById(anyString()))
                .willReturn(Optional.of(event));
        // given
        String eventId = event.getId();
        AttendeeRequestDTO request = AttendeeCreator.mockAttendeeRequestDTO(attendee);
        // when
        assertThatThrownBy(() -> undertest.registerAttendeeOnEvent(eventId, request))
                .isInstanceOf(EventFullException.class)
                        .hasMessage("Event is full");
        // then
        verify(mockEventRepository, times(1))
                .findById(event.getId());

        verify(mockAttendeeService, times(1))
                .verifyAttendeeSubscription(request.email(), eventId);

        verify(mockAttendeeService, times(1))
                .getAllAttendeesFromEvent(eventId);

        verify(mockAttendeeService, never())
                .registerAttendee(any(Attendee.class));
    }
}