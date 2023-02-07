package com.example.my_auction_rest.repository;

import com.example.my_auction_rest.entity.WinningBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WinningBetRepository extends JpaRepository<WinningBet, Long> {

    List<WinningBet> findAllByBetUserId(long id);
}
