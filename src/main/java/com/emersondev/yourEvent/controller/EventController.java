package com.emersondev.yourEvent.controller;

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
    public Event addNewEvent(@RequestBody Event event) {
        return eventService.addNewEvent(event);
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{prettyName}")
    public ResponseEntity<Object> getByPrettyname(@PathVariable("prettyName") String prettyName) {
        return eventService.getByPrettyName(prettyName);
    }

}
