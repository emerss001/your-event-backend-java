package com.emersondev.yourEvent.service;

import com.emersondev.yourEvent.dto.NewEventRequest;
import com.emersondev.yourEvent.exception.EventConflictException;
import com.emersondev.yourEvent.exception.UserOwnerNotFound;
import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.model.User;
import com.emersondev.yourEvent.repo.EventRepo;
import com.emersondev.yourEvent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;
    
    @Autowired
    private UserRepo userRepo;

    public Event addNewEvent(NewEventRequest newEventRequest) {
        String prettyName = newEventRequest.title().toLowerCase().replaceAll(" ", "-");
        
        // checando se o evento já existe
        if (eventRepo.findByPrettyName(prettyName) != null) {
            throw new EventConflictException("Event with pretty name " + prettyName + " already exists");
        }
        
        // checando se o criador do evento existe
        
        User owner = userRepo.findByEmail(newEventRequest.email());
        if (owner == null) {
            owner = new User();
            owner.setEmail(newEventRequest.email());
            owner.setName(newEventRequest.name());
            owner = userRepo.save(owner);
        }
        
        Event event = new Event();
        
        event.setOwner(owner);   
        event.setPrettyName(prettyName);
        event.setTitle(newEventRequest.title());
        event.setLocation(newEventRequest.location());
        event.setPrice(newEventRequest.price());
        event.setStartDate(newEventRequest.startDate());
        event.setEndDate(newEventRequest.endDate());
        event.setStartTime(newEventRequest.startTime());
        event.setEndTime(newEventRequest.endTime());
        
        return eventRepo.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepo.findAll();
    }
    
    public List<Event> getAllMyEvents(String email) {
        User owner = userRepo.findByEmail(email);
        if (owner == null) {
            throw new UserOwnerNotFound("User with email " + email + " not found");
        }
        
        return eventRepo.findEventsByOwner(owner);
    }

    public ResponseEntity<Object> getByPrettyName(String prettyName) {
        Event event = eventRepo.findByPrettyName(prettyName);
        if (event == null) {
            return ResponseEntity.status(404).body("Event not found");
        } else {
            return ResponseEntity.ok().body(event);

        }
    }
}
