package com.github.vinicius2335.passin.controllers;

import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.github.vinicius2335.passin.dto.checkin.CheckInIdResponseDTO;
import com.github.vinicius2335.passin.openapi.AttendeeControllerOpenApi;
import com.github.vinicius2335.passin.services.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
public class AttendeeController implements AttendeeControllerOpenApi {
    private final AttendeeService attendeeService;

    /**
     * Endpoint responsável por obter o crachá de inscrição do participante
     * @param attendeeId Identificador do participante
     * @return Crachá do participante
     */
    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(
            @PathVariable Integer attendeeId,
            UriComponentsBuilder uriBuilder
    ){
      return ResponseEntity.ok(attendeeService.getAttendeeBadge(attendeeId, uriBuilder));
    }

    /**
     * Endpoint responsável por realizar o check-in do participante no evento
     * @param attendeeId Identificador do participante
     * @return id do check-in realizado
     */
    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity<CheckInIdResponseDTO> checkInAttendee(
            @PathVariable Integer attendeeId,
            UriComponentsBuilder uriBuilder
    ){
        CheckInIdResponseDTO response = attendeeService.registerCheckInAttendee(attendeeId);

        return ResponseEntity
                .created(uriBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri())
                .body(response);
    }
}
