package com.github.vinicius2335.passin.integration;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.repositories.AttendeeRepository;
import com.github.vinicius2335.passin.repositories.CheckInRepository;
import com.github.vinicius2335.passin.repositories.EventRepository;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
import com.github.vinicius2335.passin.util.creator.CheckInCreator;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class BaseIT {
    @LocalServerPort
    protected int port;

    @Autowired
    private AttendeeRepository attendeeRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CheckInRepository checkInRepository;

    protected static Attendee attendee;
    protected static CheckIn checkIn;
    protected static Event event;

    @BeforeAll
    static void beforeAll() {
        event = EventCreator.mockEvent();
        attendee = AttendeeCreator.mockAttendee(event);
        checkIn = CheckInCreator.mockCheckIn(attendee);
    }

    protected String localUrl(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

    protected Event addEventTest(){
        return eventRepository.save(EventCreator.mockEvent());
    }

    protected Attendee addAttendeeTest(){
        Event eventCreated = addEventTest();
        return attendeeRepository.save(AttendeeCreator.mockAttendee(eventCreated));
    }

    protected CheckIn addCheckInTest(){
        addEventTest();
        Attendee attendeeCreated = addAttendeeTest();
        return checkInRepository.save(CheckInCreator.mockCheckIn(attendeeCreated));
    }
}
