package com.github.vinicius2335.passin.util.creator;

import com.github.javafaker.Faker;
import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.event.Event;

import java.time.OffsetDateTime;
import java.util.Locale;

public final class AttendeeCreator {
    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    private AttendeeCreator() {
        throw new IllegalStateException();
    }

    public static Attendee mockAttendee(Event event){
        Attendee newAttendee = new Attendee();
        newAttendee.setName(FAKER.name().fullName());
        newAttendee.setEmail(FAKER.internet().emailAddress());
        newAttendee.setCreatedAt(OffsetDateTime.now());
        newAttendee.setEvent(event);

        return newAttendee;
    }
}
