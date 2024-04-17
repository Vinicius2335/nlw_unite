package com.github.vinicius2335.passin.util.creator;

import com.github.vinicius2335.passin.domain.event.Event;

public final class EventCreator {
    private EventCreator() {
        throw new IllegalStateException();
    }

    public static Event mockEvent(){
        Event newEvent = new Event();
        newEvent.setTitle("Unite Summit");
        newEvent.setSlug("unite-summit");
        newEvent.setDetails("Um evento p/ devs apaixonados(as) por código!");
        newEvent.setMaximunAttendees(120);

        return newEvent;
    }
}
