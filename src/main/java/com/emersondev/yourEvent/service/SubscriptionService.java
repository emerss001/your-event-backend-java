package com.emersondev.yourEvent.service;

import com.emersondev.yourEvent.dto.SubscriptionRankingByUser;
import com.emersondev.yourEvent.dto.SubscriptionRankingItem;
import com.emersondev.yourEvent.dto.SubscriptionResponse;
import com.emersondev.yourEvent.exception.EventNotFoundException;
import com.emersondev.yourEvent.exception.SubscriptionConflictException;
import com.emersondev.yourEvent.exception.UserIndicatorNotFoundException;
import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.model.Subscription;
import com.emersondev.yourEvent.model.User;
import com.emersondev.yourEvent.repo.EventRepo;
import com.emersondev.yourEvent.repo.SubscriptionRepo;
import com.emersondev.yourEvent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    @Autowired
    private EventRepo eventRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    
    public SubscriptionResponse addNewSubscription(String eventName, User user, Integer userIndicatorId) {
        // Recuperar evento pelo prettyName
        Event event = eventRepo.findByPrettyName(eventName);
        if (event == null) {
            throw new EventNotFoundException("event " + eventName + " not found");
        }
        
        // verificar se o usuario indicador existe
        User userIndicator = null;
        if (userIndicatorId != null) {
            userIndicator = userRepo.findById(userIndicatorId).orElse(null);
            if (userIndicator == null) {
                throw new UserIndicatorNotFoundException("User indicator with id " + userIndicatorId + " not found");
            }
        }
        
        // verificar se o usuario já existe
        User userExists = userRepo.findByEmail(user.getEmail());
        if (userExists != null) {
            user = userExists;
        }
        // gravar usuario no banco de dados
        user = userRepo.save(user);
        
        Subscription newSubscription = new Subscription();
        newSubscription.setEvent(event);
        newSubscription.setSubscriber(user);
        newSubscription.setIndication(userIndicator);
        
        Subscription existingSubscription = subscriptionRepo.findByEventAndSubscriber(event, user);
        if (existingSubscription != null) {
            throw new SubscriptionConflictException("There is already a registration for user " + user.getName() + " at event " + event.getTitle());
        }
        
        Subscription response = subscriptionRepo.save((newSubscription));
        return new SubscriptionResponse(response.getSubscriptionNumber(), "http://yourevent.com/subscription/" + response.getEvent().getPrettyName() + "/" + response.getSubscriber().getId());
    }
    
    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        Event event = eventRepo.findByPrettyName(prettyName);
        if (event == null) {
            throw new EventNotFoundException("event " + prettyName + " not found");
        }
        
        return subscriptionRepo.generateRanking(event.getEventId());
    }
    
    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);

        SubscriptionRankingItem positionOfRanking = ranking.stream()
                .filter(item -> item.userId().equals(userId))
                .findFirst()
                .orElse(null);

        Integer position = ranking.indexOf(positionOfRanking) + 1;
        return new SubscriptionRankingByUser(positionOfRanking, position);
    }
}   
