package com.emersondev.yourEvent.service;

import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.model.Subscription;
import com.emersondev.yourEvent.model.User;
import com.emersondev.yourEvent.repo.EventRepo;
import com.emersondev.yourEvent.repo.SubscriptionRepo;
import com.emersondev.yourEvent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    @Autowired
    private EventRepo eventRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    
    public Subscription addNewSubscription(String eventName, User user) {
        // Recuperar evento pelo prettyName
        Event event = eventRepo.findByPrettyName(eventName);
        // gravar usuario no banco de dados
        user = userRepo.save(user);
        
        Subscription newSubscription = new Subscription();
        newSubscription.setEvent(event);
        newSubscription.setSubscriber(user);
        
        Subscription response = subscriptionRepo.save((newSubscription));
        return response;
    }
}   
