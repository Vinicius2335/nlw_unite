package com.github.vinicius2335.passin.openapi;

import com.github.vinicius2335.passin.dto.attendee.AttendeeIdResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeRequestDTO;
import com.github.vinicius2335.passin.dto.attendee.PageAttendeesResponse;
import com.github.vinicius2335.passin.dto.event.EventIdResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventListResponseDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface EventControllerOpenApi {
    String UUID_EXAMPLE = "c471dc12-2ca6-40e1-85f7-fad1061feb98";

    @Operation(summary = "Get all events", tags = "Event", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventListResponseDTO.class)
                    )
            )
    })
    ResponseEntity<EventListResponseDTO> getAllEvents();


    @Operation(summary = "Get an event", tags = "Event", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Id do evento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    ResponseEntity<EventResponseDTO> getEventDetail(
            @Parameter(description = "ID de um evento", example = UUID_EXAMPLE, required = true) String eventId
    );


    @Operation(summary = "Create an event", tags = "Event", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventIdResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando existir campos inválidos na requisição",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
    })
    ResponseEntity<EventIdResponseDTO> createEvent(
            @RequestBody(description = "Representa o evento a ser criado", required = true) EventRequestDTO requestDTO,
            @Parameter(hidden = true) UriComponentsBuilder uriComponentsBuilder
    );


    @PageableParameter
    @Operation(summary = "Get event attendees", tags = "Attendee", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAttendeesResponse.class)
                    )
            )
    })
    ResponseEntity<PageAttendeesResponse> getEventAttendee(
            @Parameter(description = "Id do evento selecionado", example = UUID_EXAMPLE, required = true) String eventId,
            @Parameter(description = "Nome do participante à ser filtrado se necessário", example = "Vini") String name,
            @Parameter(hidden = true) Pageable pageable
    );


    @Operation(summary = "Register an attendee", tags = "Attendee", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttendeeIdResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando existir campos inválidos na requisição",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Quando o participante já está registrado no evento",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quando o id do evento não for encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    ResponseEntity<AttendeeIdResponseDTO> registerAttendeeOnEvent(
            @Parameter(description = "Id do evento selecionado", example = UUID_EXAMPLE, required = true) String eventId,
            @RequestBody(description = "Representa um participante à ser registrado no evento",
                    required = true) AttendeeRequestDTO request,
            @Parameter(hidden = true) UriComponentsBuilder uriBuilder
    );
}
