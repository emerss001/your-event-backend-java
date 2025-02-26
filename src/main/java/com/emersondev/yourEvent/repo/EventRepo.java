package com.emersondev.yourEvent.repo;

import com.emersondev.yourEvent.model.Event;
import com.emersondev.yourEvent.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepo extends CrudRepository<Event, Integer> {
    Event findByPrettyName(String prettyName);
    List<Event> findEventsByOwner(User owner);
}
