package com.emersondev.yourEvent.service;

import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.repo.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    public Event addNewEvent(Event event) {
//        gerando preetyName
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
        return eventRepo.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepo.findAll();
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
