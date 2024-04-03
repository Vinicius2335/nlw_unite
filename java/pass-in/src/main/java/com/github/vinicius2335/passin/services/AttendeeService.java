package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeDetail;
import com.github.vinicius2335.passin.dto.attendee.AttendeesListResponseDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeBadgeDTO;
import com.github.vinicius2335.passin.repositories.AttendeeRepository;
import com.github.vinicius2335.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    /**
     * Retorna o participante encontrado pelo attendeeId
     * @param attendeeId Identificador do participante
     * @return o participante encontrado
     * @throws AttendeeNotFoundException quando o attendeeId de participante fornecido não foi encontrado no banco de dados
     */
    public Attendee getAttendeeByIdOrThrowsException(String attendeeId){
        // FIXME - ExceptionHandler
        return attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with attendeeId: " +attendeeId));
    }

    /**
     * Retorna todos os participantes  relacionado a um evento
     * @param eventId identificador do evento
     * @return lista da entidade de todos os participantes de um evento
     */
    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return attendeeRepository.findByEventId(eventId);
    }

    /**
     * Retorna todos os participantes um determinado evento
     * @param eventId identificador do evento
     * @return lista com o DTO de todos os participantes
     */
    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = getAllAttendeesFromEvent(eventId);

        List<AttendeeDetail> attendeeDetailList = attendeeList.stream()
                .map(attendee -> {
                    Optional<CheckIn> checkIn = checkInRepository.findByAttendeeId(attendee.getId());

                    OffsetDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;

                    return new AttendeeDetail(
                            attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(),
                            checkedInAt
                    );
                }).toList();

        return new AttendeesListResponseDTO(attendeeDetailList);
    }

    /**
     * Registra um novo participante no banco de dados
     * @param newAttendee participante à ser registrado
     * @return participante registrado
     */
    public Attendee registerAttendee(Attendee newAttendee){
        return attendeeRepository.save(newAttendee);
    }

    /**
     * Verifica se o participante já está inscrito no evento
     * @param email correio eletrônido do participante
     * @param eventId identificador do evento
     * @throws AttendeeAlreadyExistException quando o participante já está registrado no evento
     */
    public void verifyAttendeeSubscription(String email, String eventId) {
        // FIXME - ExceptionHandler
        Optional<Attendee> isAttendeeRegistered = attendeeRepository.findByEventIdAndEmail(eventId, email);

        if (isAttendeeRegistered.isPresent()){
            throw new AttendeeAlreadyExistException("Attendee is already registered");
        }
    }

    /**
     * Retorna o crachá do participante
     *
     * @param attendeeId Identificador do participante
     * @param uriComponentsBuilder Usado para montar a url do check-in
     * @return crachá do participante
     * @throws AttendeeNotFoundException quando o participante não foi encontrado pelo attendeeId
     */
    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = getAttendeeByIdOrThrowsException(attendeeId);

        URI uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri();

        AttendeeBadgeDTO badge = new AttendeeBadgeDTO(
                attendee.getName(),
                attendee.getEmail(),
                uri.toString(),
                attendee.getEvent().getId()
        );

        return new AttendeeBadgeResponseDTO(badge);
    }

}
