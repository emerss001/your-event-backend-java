package com.emersondev.yourEvent.service;

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

    public Event addNewEvent(Event event, String name, String email) {
        String preetyName = event.getTitle().toLowerCase().replaceAll(" ", "-");
        if (eventRepo.findByPrettyName(preetyName) != null) {
            throw new EventConflictException("Event with pretty name " + preetyName + " already exists");
        }
        
            // checando se o criador do evento existe
            User owner = userRepo.findByEmail(email);
            if (owner == null) {
                owner.setEmail(email);
                owner.setName(name);
                owner = userRepo.save(owner);
            }
            
            event.setPrettyName(preetyName);
            event.setOwner(owner);
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
