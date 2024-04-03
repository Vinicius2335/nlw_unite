package com.github.vinicius2335.passin.controllers;

import com.github.vinicius2335.passin.dto.attendee.AttendeeIdDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeRequestDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeesListResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventIdDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;
import com.github.vinicius2335.passin.services.AttendeeService;
import com.github.vinicius2335.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    /**
     * Endpoint responsável por retornar os detalhes de um evento
     * @param eventId identificador do evento que o usuário deseja visualizar os detalhes
     * @return resposta com os detalhes de um evento
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventDetail(@PathVariable String eventId){
        return ResponseEntity.ok(eventService.getEventDetail(eventId));
    }

    /**
     * Endpoint responsável por criar um novo evento
     * @param requestDTO representa um evento com os campos necessários para a sua criação
     * @return attendeeId do evento criado
     */
    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(
            @RequestBody EventRequestDTO requestDTO,
            UriComponentsBuilder uriComponentsBuilder
    ){
        EventIdDTO eventIdDTO = eventService.createEvent(requestDTO);

        // UriComponentsBuilder -> o próprio spring injeta pra nós no método
        // seria a url onde podemos consultar o evento criado
        URI uri = uriComponentsBuilder.path("/events/{attendeeId}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity
                .created(uri)
                .body(eventIdDTO);

        //return ResponseEntity
        //        .status(HttpStatus.CREATED)
        //        .body(eventService.createEvent(requestDTO))
    }

    /**
     * Endpoint responsável por listar todos os participantes relacionados a um evento
     * @param eventId identificador do evento
     * @return resposta com alista de participantes do evento solicitado
     */
    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendee(@PathVariable String eventId){
        return ResponseEntity.ok(attendeeService.getEventsAttendee(eventId));
    }

    /**
     * Endpoint responsável em registrar um participante num evento
     * @param eventId Identificador do evento
     * @param request Representa os dados necessários para registrar um novo participante
     * @return attendeeId do participante registrado no evento
     */
    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerAttendeeOnEvent(
            @PathVariable String eventId,
            @RequestBody AttendeeRequestDTO request,
            UriComponentsBuilder uriBuilder
    ){
        AttendeeIdDTO response = eventService.registerAttendeeOnEvent(eventId, request);

        return ResponseEntity
                // url onde podemos consultar esse participante criado
                .created(uriBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(response.attendeeId()).toUri())
                .body(response);
    }

}
