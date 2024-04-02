package com.github.vinicius2335.passin.dto.event;

import com.github.vinicius2335.passin.domain.event.Event;
import lombok.Getter;

@Getter
public class EventResponseDTO {
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.event = new EventDetailDTO(
                event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximunAttendees(), numberOfAttendees
        );
    }
}
