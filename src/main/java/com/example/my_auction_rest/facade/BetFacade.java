package com.example.my_auction_rest.facade;

import com.example.my_auction_rest.dto.BetDTO;
import com.example.my_auction_rest.entity.Bet;
import org.springframework.stereotype.Component;

@Component
public class BetFacade {

    public BetDTO convertBetToBetDTO(Bet bet){
        BetDTO betDTO = new BetDTO();
        betDTO.setId(bet.getId());
        betDTO.setLotId(bet.getLot().getId());
        betDTO.setAmount(bet.getAmount());
        betDTO.setUserId(bet.getUserId());
        return betDTO;
    }
}
