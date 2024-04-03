package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.github.vinicius2335.passin.domain.checkin.CheckIn;
import com.github.vinicius2335.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.github.vinicius2335.passin.dto.checkin.CheckInIdResponseDTO;
import com.github.vinicius2335.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;

    /**
     * Salva o check-in realizado pelo participante
     * @param attendee participante que realizará o check-in
     * @return id do chec-in realizado
     */
    @Transactional
    public CheckInIdResponseDTO checkInAttendee(Attendee attendee){
        verifyCheckInExists(attendee.getId());

        CheckIn checkIn = new CheckIn();
        checkIn.setAttendee(attendee);

        checkInRepository.save(checkIn);

        return new CheckInIdResponseDTO(checkIn.getId());
    }

    /**
     * Verifica se o participante já realizou o check-in no evento
     * @param attendeeId Identificador do participante
     * @throws CheckInAlreadyExistsException quando o participante já realizou o check-in
     */
    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> isCheckIn = getOptionalCheckInByAttendeeId(attendeeId);
        if (isCheckIn.isPresent()){
            throw new CheckInAlreadyExistsException("Attendee already checked in");
        }
    }

    /**
     * Retorna o check-in existente no banco a partir de um participante id
     * @param attendeeId Identificador do participante
     * @return Optional de Check-In
     */
    public Optional<CheckIn> getOptionalCheckInByAttendeeId(String attendeeId){
        return checkInRepository.findByAttendeeId(attendeeId);
    }
}
