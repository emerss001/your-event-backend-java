package com.emersondev.yourEvent.controller;

import com.emersondev.yourEvent.model.Subscription;
import com.emersondev.yourEvent.model.User;
import com.emersondev.yourEvent.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;
    
    @PostMapping("/subscription/{prettyName}")
    public ResponseEntity<Subscription> createSubscription(@PathVariable("prettyName") String prettyName, @RequestBody User subscriber){
        Subscription newSubscription = subscriptionService.addNewSubscription(prettyName, subscriber);
        if (newSubscription != null) {
            return ResponseEntity.ok(newSubscription);
        } 
        
        return ResponseEntity.badRequest().build();
    }
}
