package com.github.vinicius2335.passin.openapi;

import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.github.vinicius2335.passin.dto.checkin.CheckInIdResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface AttendeeControllerOpenApi {

    @Operation(summary = "Get an attendee badge", tags = "Attendee", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AttendeeBadgeResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Id do participante não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(
            @Parameter(description = "ID de um participante", example = "1001", required = true) Integer attendeeId,
            @Parameter(hidden = true) UriComponentsBuilder uriBuilder
    );



    @Tag(name = "Check-in")
    @Operation(summary = "Check-in Attendee", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CheckInIdResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Id do participante não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    ResponseEntity<CheckInIdResponseDTO> checkInAttendee(
            @Parameter(description = "ID de um participante", example = "1001", required = true) Integer attendeeId,
            @Parameter(hidden = true) UriComponentsBuilder uriBuilder
    );
}
