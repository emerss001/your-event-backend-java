package com.emersondev.yourEvent.repo;

import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.model.Subscription;
import com.emersondev.yourEvent.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
    Subscription findByEventAndSubscriber(Event event, User subscriber);
}
