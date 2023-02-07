package com.example.my_auction_rest.service;

import com.example.my_auction_rest.entity.User;
import com.example.my_auction_rest.entity.WinningBet;
import com.example.my_auction_rest.repository.LotRepository;
import com.example.my_auction_rest.repository.UserRepository;
import com.example.my_auction_rest.repository.WinningBetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class WinningBetService {

    private static final Logger LOG = LoggerFactory.getLogger(WinningBetService.class);

    private WinningBetRepository winningBetRepository;
    private UserRepository userRepository;
    private LotRepository lotRepository;

    @Autowired
    public WinningBetService(WinningBetRepository winningBetRepository, UserRepository userRepository, LotRepository lotRepository) {
        this.winningBetRepository = winningBetRepository;
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
    }

    public List<WinningBet> saveWinningBets(List<WinningBet> winningBets) {
        winningBets.forEach(bet -> LOG.info("Saving winning bet with bet id: " + bet.getBet().getId()));
        return winningBetRepository.saveAll(winningBets);
    }

    public List<WinningBet> getAllWinningBetsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        return winningBetRepository.findAllByBetUserId(user.getId());
    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
}
