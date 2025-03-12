package com.emersondev.yourEvent.service;

import com.emersondev.yourEvent.dto.ClicksToLinkResponse;
import com.emersondev.yourEvent.model.ClickToLink;
import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.repo.ClickToLinkRepo;
import com.emersondev.yourEvent.repo.EventRepo;
import com.emersondev.yourEvent.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClickToLinkService {
    
    @Autowired
    private ClickToLinkRepo clickToLinkRepo;
    
    @Autowired
    private EventRepo eventRepo;
    
    @Autowired
    private UserRepo userRepo;

    public void addClickToLink(String prettyName, Integer owner_id) {
        if (prettyName == null || owner_id == null) {
            throw new IllegalArgumentException("Link cannot be null");
        }
        
        Event event = eventRepo.findByPrettyName(prettyName);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        
        if (userRepo.findById(owner_id).orElse(null) == null) {
            throw new IllegalArgumentException("Owner not found");
        }
        
        ClickToLink link = new ClickToLink();
        link.setEventId(eventRepo.findById(event.getEventId()).orElse(null));
        link.setOwnerLink(userRepo.findById(owner_id).orElse(null));
         
        clickToLinkRepo.save(link);
    }
    
    public ClicksToLinkResponse getClickToLink(String prettyName, Integer owner_id) {
        if (prettyName == null || owner_id == null) {
            throw new IllegalArgumentException("Link cannot be null");
        }
        
        Event event = eventRepo.findByPrettyName(prettyName);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }

        if (userRepo.findById(owner_id).orElse(null) == null) {
            throw new IllegalArgumentException("Owner not found");
        }
        
        return new ClicksToLinkResponse(clickToLinkRepo.findByQuantityClicks(event.getEventId(), owner_id));
    }
    
    
    
}
