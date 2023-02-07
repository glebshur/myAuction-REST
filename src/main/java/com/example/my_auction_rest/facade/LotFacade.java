package com.example.my_auction_rest.facade;

import com.example.my_auction_rest.dto.LotDTO;
import com.example.my_auction_rest.entity.Lot;
import org.springframework.stereotype.Component;

@Component
public class LotFacade {

    public LotDTO convertLotToLotDTO(Lot lot){
        LotDTO lotDTO = new LotDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setActive(lot.isActive());
        lotDTO.setStartDate(lot.getStartDate());
        lotDTO.setEndDate(lot.getEndDate());
        lotDTO.setInitCost(lot.getInitCost());
        lotDTO.setUserId(lot.getUser().getId());
        return lotDTO;
    }
}
