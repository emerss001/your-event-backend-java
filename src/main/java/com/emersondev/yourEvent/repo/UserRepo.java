package com.emersondev.yourEvent.repo;

import com.emersondev.yourEvent.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
}
