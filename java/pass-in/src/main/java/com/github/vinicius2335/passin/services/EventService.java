package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.attendee.Attendee;
import com.github.vinicius2335.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.domain.event.exceptions.EventFullException;
import com.github.vinicius2335.passin.domain.event.exceptions.EventNotFoundException;
import com.github.vinicius2335.passin.dto.attendee.AttendeeIdDTO;
import com.github.vinicius2335.passin.dto.attendee.AttendeeRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventIdDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;
import com.github.vinicius2335.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    /**
     * Listagem de eventos existentes no banco de dados
     * @return lista de eventos
     */
    public List<Event> getAllEvent(){
        return eventRepository.findAll();
    }

    /**
     * Procura um evento pelo seu identificador
     * @param eventId Identificador do evento
     * @return evento
     * @throws EventNotFoundException quando evento não for encontrado pelo attendeeId fornecido
     */
    public Event getEventByIdOrThrowsException(String eventId){
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    /**
     *  Visualizar dados de um evento
     * @param eventId attendeeId do evento que o usuário deseja visualizar
     * @return detalhes do evento
     * @throws EventNotFoundException quando evento não for encontrado pelo attendeeId fornecido
     */
    public EventResponseDTO getEventDetail(String eventId){
        Event event = getEventByIdOrThrowsException(eventId);

        int numberOfAttendees = attendeeService.getAllAttendeesFromEvent(eventId).size();
        return new EventResponseDTO(event, numberOfAttendees);
    }

    /**
     * Cadastra um novo evento
     * @param request apresenta informações necessárias para a criação de um novo evento
     * @return attendeeId do evento criado
     */
    @Transactional
    public EventIdDTO createEvent(EventRequestDTO request){
        Event newEvent = new Event();
        newEvent.setTitle(request.title());
        newEvent.setDetails(request.details());
        newEvent.setMaximunAttendees(request.maximunAttendees());
        newEvent.setSlug(createSlug(request.title()));

        eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    /**
     * Aplica uma normalização que separa acentuação do caracter, depois remove todas as acentuações
     * remove tudo que não for letra ou numero,
     * troca espaços em branco por hifen
     *
     * @param text titulo do evento
     * @return slug
     */
    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}", "") // remove os acentos
                .replaceAll("[^\\w\\s]", "") // remove tudo que não for letra ou número
                .replaceAll("\\s+", "-") // troca espaço em branco por hífen
                .toLowerCase();
    }

    /**
     * Registra um participante no evento
     * @param eventId Identificador do evento
     * @param request Representa p novo participante a ser registrado no evento
     * @return attendeeId do participante registrado
     * @throws AttendeeAlreadyExistException quando o participante já esta registrado no evento
     * @throws EventNotFoundException quando o attendeeId do evento fornecido não foi encontrado no banco de dados
     * @throws EventFullException quando o participante não pode registrar-se no evento por já estar cheio
     */
    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO request){
        attendeeService.verifyAttendeeSubscription(request.email(), eventId);

        Event event = getEventByIdOrThrowsException(eventId);
        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvent(eventId);

        if (event.getMaximunAttendees() <= attendeeList.size()){
            //FIXME - ExceptionHandler
            throw new EventFullException("Event is full");
        }

        Attendee attendee = new Attendee();
        attendee.setName(request.name());
        attendee.setEmail(request.email());
        attendee.setEvent(event);

        attendeeService.registerAttendee(attendee);

        return new AttendeeIdDTO(attendee.getId());
    }
}
