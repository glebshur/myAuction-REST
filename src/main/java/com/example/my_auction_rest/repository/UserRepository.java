package com.example.my_auction_rest.repository;

import com.example.my_auction_rest.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u")
    List<User> findFirstWithPageable(Pageable pageable);

    List<User> findAllByUsernameContains(String keyword);
}
