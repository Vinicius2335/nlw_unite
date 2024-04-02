package com.github.vinicius2335.passin.services;

import com.github.vinicius2335.passin.domain.event.Event;
import com.github.vinicius2335.passin.domain.event.exceptions.EventNotFoundException;
import com.github.vinicius2335.passin.dto.event.EventIdDTO;
import com.github.vinicius2335.passin.dto.event.EventRequestDTO;
import com.github.vinicius2335.passin.dto.event.EventResponseDTO;
import com.github.vinicius2335.passin.repositories.AttendeeRepository;
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
    private final AttendeeRepository attendeeRepository;

    /**
     * Listagem de eventos
     * @return lista de eventos
     */
    public List<Event> getAllEvent(){
        return eventRepository.findAll();
    }

    /**
     *  Visualizar dados de um evento
     * @param eventId id do evento que o usuário deseja visualizar
     * @return detalhes do evento
     */
    public EventResponseDTO getEventDetailOrThrowsException(String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        int numberOfAttendees = attendeeRepository.findByEventId(eventId).size();
        return new EventResponseDTO(event, numberOfAttendees);
    }

    /**
     * Cadastra um novo evento
     * @param request apresenta informações necessárias para a criação de um novo evento
     * @return id do evento criado
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
                .replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "") // remove os acentos
                .replaceAll("[^\\w\\s]", "") // remove tudo que não for letra ou número
                .replaceAll("\\s+", "-") // troca espaço em branco por hífen
                .toLowerCase();

    }
}
