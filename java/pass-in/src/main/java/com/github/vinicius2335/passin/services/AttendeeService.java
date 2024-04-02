package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.dto.attendee.AttendeeDetail;
import com.github.vinicius2335.passin.dto.attendee.AttendeesListResponseDTO;
import com.github.vinicius2335.passin.repositories.AttendeeRepository;
import com.github.vinicius2335.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

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
}
