package com.github.vinicius2335.passin.util.creator;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;

import java.time.OffsetDateTime;

public final class CheckInCreator {
    private CheckInCreator() {
        throw new IllegalStateException("Utility Class");
    }

    public static CheckIn mockCheckIn(Attendee attendee){
        CheckIn checkIn = new CheckIn();
        checkIn.setId(1);
        checkIn.setAttendee(attendee);
        checkIn.setCreatedAt(OffsetDateTime.now());

        return checkIn;
    }
}
