package com.emersondev.yourEvent.controller;

import com.emersondev.yourEvent.dto.ErrorMessage;
import com.emersondev.yourEvent.dto.NewEventRequest;
import com.emersondev.yourEvent.exception.EventConflictException;
import com.emersondev.yourEvent.exception.UserOwnerNotFound;
import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Operation(description = "Criar um novo evento")
    @PostMapping("/events")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento criado com sucesso", content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "409", description = "Evento com pretty name já existente", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> addNewEvent(@RequestBody NewEventRequest newEventRequest) {
        try {
            Event newEvent = eventService.addNewEvent(newEventRequest);
            if (newEvent != null) {
                return ResponseEntity.ok(newEvent);
            }
        } catch (EventConflictException error) {
            return ResponseEntity.status(409).body(new ErrorMessage(error.getMessage()));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(description = "Buscar todos os eventos")
    @GetMapping("/events")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eventos encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Event.class)))),
    })
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Operation(description = "Buscar um evento pelo seu pretty name")
    @GetMapping("/events/{prettyName}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado", content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> getByPrettyname(@PathVariable("prettyName") String prettyName) {
        return eventService.getByPrettyName(prettyName);
    }
    
    @Operation(description = "Buscar os eventos criados por um determinado usuário")
    @GetMapping("/events/myevents/{email}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eventos encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Event.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> getAllMyEvents(@PathVariable("email") String email) {
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
