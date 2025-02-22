package com.emersondev.yourEvent.repo;

import com.emersondev.yourEvent.model.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer> {
}
