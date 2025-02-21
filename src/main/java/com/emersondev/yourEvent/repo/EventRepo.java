package com.emersondev.yourEvent.repo;

import com.emersondev.yourEvent.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<Event, Integer> {
    Event findByPrettyName(String prettyName);
}
