package com.github.vinicius2335.passin.integration.controllers;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.dto.attendee.AttendeeRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.integration.BaseIT;
import com.github.vinicius2335.passin.util.creator.AttendeeCreator;
import com.github.vinicius2335.passin.util.creator.EventCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Log4j2
@DisplayName("Integration test for event controller")
class EventControllerIT extends BaseIT {

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = localUrl("/events");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("getAllEvents() return all events in database")
    void whenGetAllEvents_thenReturnStatus200() {
        addEventTest();

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("events", Matchers.hasSize(1))
                .body("events[0].event.title", Matchers.equalTo(event.getTitle()))
                .log().all();
    }

    @Test
    @DisplayName("getEventDetail() return event details response")
    void givenEventId_whenGetEventDetail_thenReturnStatus200() {
        Event eventCreated = addEventTest();

        given()
                .pathParams("eventId", eventCreated.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .get("/{eventId}")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("events", Matchers.hasSize(1))
                .body("events[0].event.title", Matchers.equalTo(event.getTitle()))
                .log().all();
    }

    @Test
    @DisplayName("getEventDetail() throws exception when event not found by id")
    void givenEventId_whenGetEventDetail_thenEventNotFoundReturnStatus404() {

        given()
                .pathParams("eventId", event.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .get("/{eventId}")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.containsString("Event not found with ID"))
                .log().all();
    }

    @Test
    @DisplayName("createEvent() create an new event")
    void givenEventRequest_whenCreateEvent_thenReturnStatus201() {
        EventRequestDTO request = EventCreator.mockEventRequest(event);

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();
    }

    @Test
    @DisplayName("createEvent() throws badRequest when title of event already exist")
    void givenEventRequest_whenCreateEvent_thenEventTitleAlreadyExistsReturnStatus400() {
        addEventTest();
        EventRequestDTO request = EventCreator.mockEventRequest(event);

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("Some field of the request has a resource in use"))
                .log().all();
    }

    @Test
    @DisplayName("createEvent() throws badRequest when event request have invalid fields")
    void givenEventRequest_whenCreateEvent_thenEventRequestHaveInvalidFieldsReturnStatus400() {
        addEventTest();
        EventRequestDTO invalidRequest = EventCreator.mockInvalidEventRequest();

        given()
                .body(invalidRequest)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo("One or more fields are invalid"))
                .log().all();
    }

    @Test
    @DisplayName("getEventAttendee() return Page of attendees related to eventId")
    void givenEventId_whenGetEventAttendee_thenReturnStatus200() {
        Attendee attendeeCreated = addAttendeeTest();

        given()
                .pathParams("eventId", attendeeCreated.getEvent().getId())
                .queryParam("name", attendeeCreated.getName())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().all()
        .when()
                .get("/{eventId}/attendees")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("currentItens", Matchers.is(1))
                .body("attendees[0].name", Matchers.equalTo(attendeeCreated.getName()))
                .log().all();
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() register a attendee in event")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenReturnStatus201() {
        Event eventCreated = addEventTest();
        AttendeeRequestDTO request = AttendeeCreator.mockAttendeeRequestDTO(attendee);

        given()
                .pathParams("eventId", eventCreated.getId())
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post("/{eventId}/attendees")
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("attendeeId", Matchers.is(1001))
                .log().all();
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() throws exception when event not found by id")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenEventNotFoundByIdReturnStatus404() {
        addEventTest();
        AttendeeRequestDTO request = AttendeeCreator.mockAttendeeRequestDTO(attendee);

        given()
                .pathParams("eventId", UUID.randomUUID())
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post("/{eventId}/attendees")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.containsString("Event not found with ID"))
                .log().all();
    }

    @Test
    @DisplayName("registerAttendeeOnEvent() throws exception when attendee request have invalid fields")
    void givenEventIdAndAttendeeRequest_whenRegisterAttendeeOnEvent_thenInvalidRequestReturnStatus400() {
        Event eventCreated = addEventTest();
        AttendeeRequestDTO invalidRequest = AttendeeCreator.mockInvalidAttendeeRequestDTO();

        given()
                .pathParams("eventId", eventCreated.getId())
                .body(invalidRequest)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post("/{eventId}/attendees")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.containsString("One or more fields are invalid"))
                .log().all();
    }
}