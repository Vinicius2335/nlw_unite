package com.github.vinicius2335.passin.controllers;

import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.github.vinicius2335.passin.services.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {
    private final AttendeeService attendeeService;

    /**
     * Endpoint responsável por obter o crachá de inscrição do participante
     * @param attendeeId Identificador do participante
     * @return Crachá do participante
     */
    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(
            @PathVariable String attendeeId,
            UriComponentsBuilder uriBuilder
    ){
      return ResponseEntity.ok(attendeeService.getAttendeeBadge(attendeeId, uriBuilder));
    }
}
