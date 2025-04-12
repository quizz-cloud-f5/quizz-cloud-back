package org.quizzcloud.backend.auth.service;

import lombok.RequiredArgsConstructor;
import org.quizzcloud.backend.auth.model.User;
import org.quizzcloud.backend.auth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUsersService {
    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
