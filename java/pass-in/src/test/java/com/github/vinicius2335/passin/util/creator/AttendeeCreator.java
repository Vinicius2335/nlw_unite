package com.github.vinicius2335.passin.util.creator;

import com.github.javafaker.Faker;
import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeDetail;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Locale;

public final class AttendeeCreator {
    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    private AttendeeCreator() {
        throw new IllegalStateException();
    }

    public static Attendee mockAttendee(Event event){
        Attendee newAttendee = new Attendee();
        newAttendee.setId(1001);
        newAttendee.setName(FAKER.name().fullName());
        newAttendee.setEmail(FAKER.internet().emailAddress());
        newAttendee.setCreatedAt(OffsetDateTime.now());
        newAttendee.setEvent(event);

        return newAttendee;
    }

    public static AttendeeDetail mockAttendeeDetail(Attendee attendee, CheckIn checkIn){
        return new AttendeeDetail(
                attendee.getId(),
                attendee.getName(),
                attendee.getEmail(),
                attendee.getCreatedAt(),
                checkIn.getCreatedAt()
        );
    }

    public static AttendeeBadgeResponseDTO mockAttendeeBadgeResponseDTO(Attendee attendee){
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        URI uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendee.getId()).toUri();
        return new AttendeeBadgeResponseDTO(
                new AttendeeBadgeDTO(
                        attendee.getName(),
                        attendee.getEmail(),
                        uri.toString(),
                        attendee.getEvent().getId(),
                        attendee.getEvent().getTitle(),
                        attendee.getId()
                )
        );
    }
}
