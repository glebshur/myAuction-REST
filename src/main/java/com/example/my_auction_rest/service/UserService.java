package com.example.my_auction_rest.service;

import com.example.my_auction_rest.dto.UserDTO;
import com.example.my_auction_rest.entity.User;
import com.example.my_auction_rest.entity.enums.Role;
import com.example.my_auction_rest.exception.UserCreateException;
import com.example.my_auction_rest.exception.UserNotFoundException;
import com.example.my_auction_rest.exception.UserUpdateException;
import com.example.my_auction_rest.payload.request.SignUpRequest;
import com.example.my_auction_rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignUpRequest userIn) {
        User user = new User();
        user.setUsername(userIn.getUsername());
        user.setEmail(userIn.getEmail());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        LOG.info("Creating user: " + user.getUsername());
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            LOG.error(ex.getMessage());
            throw new UserCreateException("A user with this username or email already exists");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getFirst10Users() {
        Pageable topTen = PageRequest.of(0, 10);
        return userRepository.findFirstWithPageable(topTen);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User cannot be found with id: " + id));
    }

    public List<User> getUsersByKeyword(String keyword) {
        return userRepository.findAllByUsernameContains(keyword);
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setUsername(userDTO.getUsername());

        LOG.info("Saving user with id: " + user.getId());
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            LOG.error(ex.getMessage());
            throw new UserUpdateException("A user with this username (" + userDTO.getUsername() + ") already exists");
        }
    }

    public User updateUser(Long userId, UserDTO userDTO) {
        User user = getUserById(userId);
        // ROLE_ADMIN cannot be added or deleted from client side
        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            userDTO.getRoles().add(Role.ROLE_ADMIN);
        } else {
            userDTO.getRoles().remove(Role.ROLE_ADMIN);
        }
        // ROLE_USER cannot be removed
        userDTO.getRoles().add(Role.ROLE_USER);
        user.setRoles(userDTO.getRoles());

        LOG.info("Saving user with id: " + user.getId());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
}
