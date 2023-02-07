package com.example.my_auction_rest.repository;


import com.example.my_auction_rest.entity.Bet;
import com.example.my_auction_rest.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    Optional<Bet> findByIdAndArchivalIsFalse(Long id);

    List<Bet> findAllByLotAndArchivalIsFalse(Lot lot);

}
