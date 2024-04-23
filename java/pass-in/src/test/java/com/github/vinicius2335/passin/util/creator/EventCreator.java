package com.github.vinicius2335.passin.util.creator;

import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.dto.event.EventListResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;

import java.util.List;

public final class EventCreator {
    private EventCreator() {
        throw new IllegalStateException();
    }

    public static Event mockEvent(){
        Event newEvent = new Event();
        newEvent.setId("5024ca87-34e8-45f0-a4ca-8734e8b5f084");
        newEvent.setTitle("Unite Summit");
        newEvent.setSlug("unite-summit");
        newEvent.setDetails("Um evento p/ devs apaixonados(as) por código!");
        newEvent.setMaximunAttendees(120);

        return newEvent;
    }

    public static EventResponseDTO mockEventResponseDTO(Event event){
        return new EventResponseDTO(
          event,
          1
        );
    }

    public static EventListResponseDTO mockEventListResponseDTO(Event event) {
        return new EventListResponseDTO(
                List.of(mockEventResponseDTO(event))
        );
    }

    public static EventRequestDTO mockEventRequest(Event event){
        return new EventRequestDTO(
                event.getTitle(),
                event.getDetails(),
                event.getMaximunAttendees()
        );
    }
}
