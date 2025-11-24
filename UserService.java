package com.guvi.mindfulness.service;

import com.guvi.mindfulness.dao.UserDAO;
import com.guvi.mindfulness.exception.DataAccessException;
import com.guvi.mindfulness.exception.ValidationException;
import com.guvi.mindfulness.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Business layer around {@link UserDAO}. Responsible for validating inputs before delegating to JDBC.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public long registerUser(User user) throws ValidationException {
        validateUser(user);
        try {
            return userDAO.insert(user);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create user", e);
        }
    }

    public List<User> listUsers() {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to list users", e);
        }
    }

    public User getUser(long id) {
        try {
            return userDAO.findById(id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch user", e);
        }
    }

    public boolean updateUser(User user) throws ValidationException {
        if (user.getId() <= 0) {
            throw new ValidationException("User id is required for update");
        }
        validateUser(user);
        try {
            return userDAO.update(user);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update user", e);
        }
    }

    public boolean deleteUser(long id) {
        try {
            return userDAO.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete user", e);
        }
    }

    private void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User payload cannot be null");
        }
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new ValidationException("Full name is mandatory");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("A valid email is required");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new ValidationException("Password must contain at least 6 characters");
        }
    }
}
package com.guvi.mindfulness.service;

import com.guvi.mindfulness.dto.UserRegistrationRequest;
import com.guvi.mindfulness.dto.UserResponse;
import com.guvi.mindfulness.model.Role;
import com.guvi.mindfulness.model.User;
import com.guvi.mindfulness.repository.RoleRepository;
import com.guvi.mindfulness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for user-related business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        // Assign default role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(userRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        return mapToUserResponse(savedUser);
    }

    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    @Cacheable(value = "users", key = "#username")
    public User findByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        log.debug("Fetching user by username or email: {}", usernameOrEmail);
        return userRepository.findByUsername(usernameOrEmail)
                .orElse(userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "users", key = "#id")
    public UserResponse updateUser(Long id, UserRegistrationRequest request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setBio(user.getBio());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}


