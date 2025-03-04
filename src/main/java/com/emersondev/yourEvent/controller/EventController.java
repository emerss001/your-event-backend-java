package com.emersondev.yourEvent.controller;

import com.emersondev.yourEvent.dto.ErrorMessage;
import com.emersondev.yourEvent.exception.EventConflictException;
import com.emersondev.yourEvent.exception.UserOwnerNotFound;
import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<Object> addNewEvent(@RequestBody Event event, @RequestParam String name, @RequestParam String email) {
        try {
            Event newEvent = eventService.addNewEvent(event, name, email);
            if (newEvent != null) {
                return ResponseEntity.ok(newEvent);
            }
        } catch (EventConflictException error) {
            return ResponseEntity.status(409).body(new ErrorMessage(error.getMessage()));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Object> getByPrettyname(@PathVariable("prettyName") String prettyName) {
        return eventService.getByPrettyName(prettyName);
    }
    
    @GetMapping("/events/myevents")
    public ResponseEntity<Object> getAllMyEvents(@RequestParam String email) {
        System.out.println(email);
        try {
            List<Event> myEvents = eventService.getAllMyEvents(email);
            if (myEvents != null) {
                return ResponseEntity.ok(myEvents);
            }
        } catch (UserOwnerNotFound error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
        
        return ResponseEntity.badRequest().build();
    }

}
