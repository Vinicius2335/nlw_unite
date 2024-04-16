package com.github.vinicius2335.passin.controllers;

import com.github.vinicius2335.passin.dto.attendee.AttendeeIdResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeRequestDTO;
import com.github.vinicius2335.passin.dto.attendee.PageAttendeesResponse;
import com.github.vinicius2335.passin.dto.event.EventIdResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventListResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;
import com.github.vinicius2335.passin.openapi.EventControllerOpenApi;
import com.github.vinicius2335.passin.services.AttendeeService;
import com.github.vinicius2335.passin.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
public class EventController implements EventControllerOpenApi {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    /**
     * Endpont responsável por retornar todos os eventos
     * @return todos os eventos registrados no banco
     */
    @GetMapping
    public ResponseEntity<EventListResponseDTO> getAllEvents(){
        return ResponseEntity.ok(eventService.getAllEvent());
    }

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
    public ResponseEntity<EventIdResponseDTO> createEvent(
            @RequestBody @Valid EventRequestDTO requestDTO,
            UriComponentsBuilder uriComponentsBuilder
    ){
        EventIdResponseDTO eventIdResponseDTO = eventService.createEvent(requestDTO);

        // UriComponentsBuilder -> o próprio spring injeta pra nós no método
        // seria a url onde podemos consultar o evento criado
        URI uri = uriComponentsBuilder.path("/events/{attendeeId}").buildAndExpand(eventIdResponseDTO.eventId()).toUri();

        return ResponseEntity
                .created(uri)
                .body(eventIdResponseDTO);

        //return ResponseEntity
        //        .status(HttpStatus.CREATED)
        //        .body(eventService.createEvent(requestDTO))
    }

    /**
     * Endpoint responsável por listar todos os participantes relacionados a um evento
     *
     * @param eventId  Identificador do evento
     * @param name     nome desejado na busca
     * @param pageable Objeto que representa a página ou o número de itens por página que deseja retornar da paginação
     * @return Objeto que representa uma paginação contendo uma lista de AttendeeDetails
     */
    @GetMapping("/{eventId}/attendees")
    public ResponseEntity<PageAttendeesResponse> getEventAttendee(
            @PathVariable String eventId,
            @RequestParam(required = false, defaultValue = "") String name,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(attendeeService.getEventsAttendee(eventId, name, pageable));
    }

    /**
     * Endpoint responsável em registrar um participante num evento
     * @param eventId Identificador do evento
     * @param request Representa os dados necessários para registrar um novo participante
     * @return attendeeId do participante registrado no evento
     */
    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdResponseDTO> registerAttendeeOnEvent(
            @PathVariable String eventId,
            @RequestBody @Valid AttendeeRequestDTO request,
            UriComponentsBuilder uriBuilder
    ){
        AttendeeIdResponseDTO response = eventService.registerAttendeeOnEvent(eventId, request);

        return ResponseEntity
                // url onde podemos consultar esse participante criado
                .created(uriBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(response.attendeeId()).toUri())
                .body(response);
    }

}
