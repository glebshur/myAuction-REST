package com.example.my_auction_rest.service;

import com.example.my_auction_rest.dto.LotDTO;
import com.example.my_auction_rest.entity.Bet;
import com.example.my_auction_rest.entity.Lot;
import com.example.my_auction_rest.entity.User;
import com.example.my_auction_rest.entity.enums.Role;
import com.example.my_auction_rest.exception.LotCreateException;
import com.example.my_auction_rest.exception.LotDeleteException;
import com.example.my_auction_rest.exception.LotNotFoundException;
import com.example.my_auction_rest.exception.LotUpdateException;
import com.example.my_auction_rest.repository.BetRepository;
import com.example.my_auction_rest.repository.LotRepository;
import com.example.my_auction_rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Class that implements the business logic of the lots
@Service
public class LotService {

    private static final Logger LOG = LoggerFactory.getLogger(LotService.class);

    private LotRepository lotRepository;
    private UserRepository userRepository;
    private BetRepository betRepository;

    @Autowired
    public LotService(LotRepository lotRepository, UserRepository userRepository, BetRepository betRepository) {
        this.lotRepository = lotRepository;
        this.userRepository = userRepository;
        this.betRepository = betRepository;
    }

    public Lot createLot(LotDTO lotDTO, Principal principal) {
        User user = getUserByPrincipal(principal);

        Lot lot = new Lot();
        lot.setTitle(lotDTO.getTitle());
        lot.setDescription(lotDTO.getDescription());
        lot.setInitCost(lotDTO.getInitCost());

        // Check if start date is after end date
        if (LocalDateTime.now().isBefore(lotDTO.getEndDate())) {
            lot.setStartDate(LocalDateTime.now());
            lot.setEndDate(lotDTO.getEndDate());
        } else {
            throw new LotCreateException("Start date must be after end date");
        }

        lot.setBets(null);
        lot.setUser(user);
        lot.setActive(true);
        lot.setArchival(false);

        LOG.info("Saving lot for user: " + user.getUsername());
        return lotRepository.save(lot);
    }

    public List<Lot> saveLots(List<Lot> lots){
        lots.forEach(lot -> LOG.info("Saving changes for lot with id: " + lot.getId()));
        return lotRepository.saveAll(lots);
    }

    public List<Lot> getAllLots(boolean isActive) {
        return lotRepository.findAllByActiveAndArchivalIsFalse(isActive);
    }

    public Lot getLotById(Long lotId) {
        return lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found"));
    }

    public List<Lot> getAllLotsForUser(Principal principal, boolean active) {
        User user = getUserByPrincipal(principal);
        return lotRepository.findAllByUserAndActiveAndArchivalIsFalseOrderByStartDateDesc(user, active);
    }

    public List<Lot> getLotsUserParticipateIn(Principal principal, boolean active){
        User user = getUserByPrincipal(principal);
        return lotRepository.findDistinctLotByUserIdAndArchivalIsFalse(user.getId()).stream()
                .filter(lot -> lot.isActive() == active)
                .collect(Collectors.toList());
    }

    public List<Lot> getFirst10Lots(){
        return lotRepository.findFirst10ByActiveIsTrueAndArchivalIsFalseOrderByStartDateDesc();
    }

    public void deleteLot(Long lotId, Principal principal) {
        User currUser = getUserByPrincipal(principal);
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(()->new LotNotFoundException("Lot with id " + lotId + " cannot be found"));

        if(!lot.isActive()){
            throw new LotDeleteException("Lot isn't active");
        }

        // Check deletion rights
        if(lot.getUser().equals(currUser) || currUser.getRoles().contains(Role.ROLE_ADMIN)) {
            lot.setArchival(true);
            List<Bet> modifiedBets = new ArrayList<>();
            lot.getBets()
                    .forEach(bet -> {
                        bet.setArchival(true);
                        modifiedBets.add(bet);
                    });
            betRepository.saveAll(modifiedBets);
            LOG.info("Deleting lot with id: " + lot.getId());
            lotRepository.save(lot);
        }
        else {
            throw new AccessDeniedException("Only admin can delete data that is not his");
        }
    }

    public List<Lot> getLotsByKeyword(String keyword){
        return lotRepository.findAllByTitleContainsAndArchivalIsFalse(keyword);
    }

    public List<Lot> getLotsByKeywordAndActivity(String keyword, boolean active) {
        return lotRepository.findAllByTitleContainsAndActiveAndArchivalIsFalse(keyword, active);
    }


    public Lot updateLot(Long id, LotDTO lotDTO, Principal principal) {
        User currUser = getUserByPrincipal(principal);
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(id)
                .orElseThrow(()->new LotNotFoundException("Lot cannot be found with id: " + id));

        if(!lot.getUser().equals(currUser)){
            throw new LotUpdateException("Only owner can change the lot");
        }
        if(!lot.isActive()){
            throw new LotUpdateException("Inactive lot cannot be changed");
        }

        lot.setTitle(lotDTO.getTitle());
        lot.setDescription(lotDTO.getDescription());
        LOG.info("Updating lot with id: " + lot.getId());
        return lotRepository.save(lot);
    }


    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }


}
