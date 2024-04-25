package com.github.vinicius2335.passin.integration.controllers;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.integration.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;


@DisplayName("Integration test for attendee controller")
class AttendeeControllerIT extends BaseIT {

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = localUrl("/attendees");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @DisplayName("getAttendeeBadge() return attendee badge")
    void givenAttendeeId_whenGetAttendeeBadge_thenReturnAttendeeBadgeAndStatus200() {
        Attendee attendeeTest = addAttendeeTest();

        given()
                .pathParams("attendeeId", attendeeTest.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{attendeeId}/badge")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("badge.name", Matchers.equalTo(attendeeTest.getName()))
                .log().all();
    }

    @Test
    @DisplayName("getAttendeeBadge() Throws exception when attendee not found by id")
    void givenAttendeeId_whenGetAttendeeBadge_thenAttendeeNotFoundReturnStatus404() {

        given()
                .pathParams("attendeeId", attendee.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{attendeeId}/badge")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.containsString("Attendee not found with attendeeId"))
                .log().all();
    }

    @Test
    @DisplayName("checkInAttendee() checked attendee in event")
    void givenAttendeeId_whenCheckInAttendee_thenReturnStatus201() {
        Attendee attendeeTest = addAttendeeTest();

        given()
                .pathParams("attendeeId", attendeeTest.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{attendeeId}/check-in")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("$", Matchers.notNullValue())
                .log().all();
    }

    @Test
    @DisplayName("checkInAttendee() throws exception when attendee not found by id")
    void givenAttendeeId_whenCheckInAttendee_thenAttendeeNotFoundReturnStatus404() {

        given()
                .pathParams("attendeeId", attendee.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{attendeeId}/check-in")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.containsString("Attendee not found with attendeeId"))
                .log().all();
    }
}