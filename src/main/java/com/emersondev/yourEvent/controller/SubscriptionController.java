package com.emersondev.yourEvent.controller;

import com.emersondev.yourEvent.dto.ErrorMessage;
import com.emersondev.yourEvent.dto.SubscriptionResponse;
import com.emersondev.yourEvent.exception.EventNotFoundException;
import com.emersondev.yourEvent.exception.SubscriptionConflictException;
import com.emersondev.yourEvent.exception.UserIndicatorNotFoundException;
import com.emersondev.yourEvent.model.Subscription;
import com.emersondev.yourEvent.model.User;
import com.emersondev.yourEvent.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
    
    @PostMapping({"/subscription/{prettyName}", "/subscription/{prettyName}/{userIndicatorId}"})
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
    
    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<Object> getSubscriptionRankingByEvent(@PathVariable("prettyName") String prettyName) {
        try {
            return ResponseEntity.ok(subscriptionService.getCompleteRanking(prettyName).subList(0, 3));
        } catch (EventNotFoundException error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
    }
    
    @GetMapping("/subscription/{prettyName}/ranking/{userId}")
    public ResponseEntity<Object> getSubscriptionRankingByUser(@PathVariable("prettyName") String prettyName, @PathVariable("userId") Integer userId) {
        try {
            return ResponseEntity.ok(subscriptionService.getRankingByUser(prettyName, userId));
        } catch (Exception error) {
            return ResponseEntity.status(404).body(new ErrorMessage(error.getMessage()));
        }
    }
}
