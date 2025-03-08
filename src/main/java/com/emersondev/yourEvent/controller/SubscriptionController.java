package com.emersondev.yourEvent.controller;

import com.emersondev.yourEvent.dto.ErrorMessage;
import com.emersondev.yourEvent.dto.SubscriptionRankingItem;
import com.emersondev.yourEvent.dto.SubscriptionResponse;
import com.emersondev.yourEvent.exception.EventNotFoundException;
import com.emersondev.yourEvent.exception.SubscriptionConflictException;
import com.emersondev.yourEvent.exception.UserIndicatorNotFoundException;
import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.model.Subscription;
import com.emersondev.yourEvent.model.User;
import com.emersondev.yourEvent.service.SubscriptionService;
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
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
    
    @Operation(description = "Criar uma nova inscrição autônoma em um evento quando o userId não é informado, quando informado, a inscrição é feita por indicação")
    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userIndicatorId}"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inscrição criada com sucesso", content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Inscrição conflitante", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Indicador não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> createSubscription(@PathVariable("prettyName") String prettyName, @PathVariable(value = "userIndicatorId", required = false) Integer userIndicatorId, @RequestBody User subscriber){
        try {
            SubscriptionResponse newSubscription = subscriptionService.addNewSubscription(prettyName, subscriber, userIndicatorId);
            if (newSubscription != null) {
                return ResponseEntity.ok(newSubscription);
            }

        } catch (EventNotFoundException error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        } catch (SubscriptionConflictException error) {
            return ResponseEntity.status(409).body(new ErrorMessage(error.getMessage()));
        } catch (UserIndicatorNotFoundException error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
        
        return ResponseEntity.badRequest().build();
    }
    
    @Operation(description = "Busca o ranking de indicações de um evento pelo seu pretty name")
    @GetMapping("/subscription/{prettyName}/ranking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranking encontrado", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubscriptionRankingItem.class)))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> getSubscriptionRankingByEvent(@PathVariable("prettyName") String prettyName) {
        try {
            List<SubscriptionRankingItem> ranking = subscriptionService.getCompleteRanking(prettyName);
            int endIndex = Math.min(ranking.size(), 3);
            return ResponseEntity.ok(ranking.subList(0, endIndex));        
        } catch (EventNotFoundException error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
    }
    
    @Operation(description = "Busca a posição no ranking de incrições de um determinado usuário pelo seu id em um evento pelo seu pretty name")
    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Posição no ranking encontrada", content = @Content(schema = @Schema(implementation = SubscriptionRankingItem.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
    })
    public ResponseEntity<Object> getSubscriptionRankingByUser(@PathVariable("prettyName") String prettyName, @PathVariable("userId") Integer userId) {
        try {
            return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
        } catch (Exception error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
    }
}
