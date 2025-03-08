package com.emersondev.yourEvent.controller;

import com.emersondev.yourEvent.dto.ClicksToLinkResponse;
import com.emersondev.yourEvent.dto.ErrorMessage;
import com.emersondev.yourEvent.service.ClickToLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClickToLinkController {
    
    @Autowired
    private ClickToLinkService clickToLinkService;
    
    @Operation(description = "Adicionar um click a um link")    
    @PostMapping("/clicks/{event_id}/{owner_id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Link clicado", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Evento ou dono do link não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> addClickToLink(@PathVariable("event_id") Integer event_id, @PathVariable("owner_id") Integer owner_id) {
        try {
            clickToLinkService.addClickToLink(event_id, owner_id);
            return ResponseEntity.ok("Link clicked");
        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
    }
    
    @GetMapping("/clicks/{event_id}/{owner_id}")
    public ResponseEntity<Object> getClicksToLink(@PathVariable("event_id") Integer event_id, @PathVariable("owner_id") Integer owner_id) {
        try {
            ClicksToLinkResponse clicks = clickToLinkService.getClickToLink(event_id, owner_id);
            return ResponseEntity.ok(clicks);
        } catch (IllegalArgumentException error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
    }
}
