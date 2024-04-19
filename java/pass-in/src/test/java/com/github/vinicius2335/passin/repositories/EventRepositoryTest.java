package com.github.vinicius2335.passin.repositories;

import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Unit testing for event repository")
class EventRepositoryTest {
    @Autowired
    private EventRepository undertest;

    private Event event;

    @BeforeEach
    void setUp() {
        event = EventCreator.mockEvent();
    }

    @Test
    @DisplayName("save() insert new event")
    void givenEvent_whenSave_thenInsertAnNewEvent(){
        // given
        Event newEvent = event;
        // when
        Event expected = undertest.save(newEvent);
        // then
        assertThat(expected)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(event);
    }

    private Event initDatabase(){
        return undertest.save(event);
    }

    @Test
    @DisplayName("findAll() return list of Events")
    void whenFindAll_thenReturnListEvents(){
        Event eventSaved = initDatabase();
        // when
        List<Event> expected = undertest.findAll();
        // then
        assertThat(expected)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(eventSaved);
    }

    @Test
    @DisplayName("findAll() return empty list when dont have any event registered in database")
    void whenFindAll_thenReturnEmptyListOfEvents(){
        // when
        List<Event> expected = undertest.findAll();
        // then
        assertThat(expected)
                .isEmpty();
    }

    @Test
    @DisplayName("findById() return Optional Event")
    void givenEventId_whenFindById_thenReturnOptionalEvent(){
        Event eventSaved = initDatabase();
        // given
        String eventId = eventSaved.getId();
        // when
        Optional<Event> expected = undertest.findById(eventId);
        // then
        assertThat(expected)
                .isNotNull()
                .isNotEmpty()
                .hasValueSatisfying(
                        eventTest ->
                        assertThat(eventTest)
                                .usingRecursiveComparison()
                                .isEqualTo(eventSaved)
                );
    }

    @Test
    @DisplayName("findById() return optional empty when no event was found by id")
    void givenEventId_whenFindById_thenReturnOptionalEmptyEvent(){
        // given
        String eventId = event.getId();
        // when
        Optional<Event> expected = undertest.findById(eventId);
        // then
        assertThat(expected)
                .isEmpty();
    }

}