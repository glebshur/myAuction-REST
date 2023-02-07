package com.example.my_auction_rest.controller;

import com.example.my_auction_rest.dto.BetDTO;
import com.example.my_auction_rest.entity.Bet;
import com.example.my_auction_rest.facade.BetFacade;
import com.example.my_auction_rest.payload.response.MessageResponse;
import com.example.my_auction_rest.service.BetService;
import com.example.my_auction_rest.validator.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/bet")
@Tag(name = "Bet controller",
description = "Controller allows to create, delete bets or to get info about them")
public class BetController {

    @Autowired
    private BetFacade betFacade;
    @Autowired
    private BetService betService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{lotId}/create")
    @Operation(summary = "Bet creation",
    description = "Allows user to make bet on specific lot")
    public ResponseEntity<Object> createBet(@Valid @RequestBody BetDTO betDTO,
                                            BindingResult bindingResult,
                                            @PathVariable("lotId") Long lotId,
                                            Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        Bet bet = betService.createBet(lotId, betDTO, principal);
        BetDTO createdBet = betFacade.convertBetToBetDTO(bet);
        return new ResponseEntity<>(createdBet, HttpStatus.OK);
    }

    @GetMapping("/{betId}")
    @Operation(summary = "Bet info",
    description = "Gives info about specific bet")
    public ResponseEntity<BetDTO> getBet(@PathVariable("betId") Long betId){
        Bet bet = betService.getBetById(betId);
        BetDTO betDTO = betFacade.convertBetToBetDTO(bet);
        return new ResponseEntity<>(betDTO, HttpStatus.OK);
    }

    @GetMapping("/{lotId}/all")
    @Operation(summary = "All bets",
    description = "Gives all bets for specific lot")
    public ResponseEntity<List<BetDTO>> getBetsForLot(@PathVariable("lotId") Long lotId){
        List<BetDTO> betDTOList = betService.getAllBetsForLot(lotId).stream()
                .map(betFacade::convertBetToBetDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(betDTOList, HttpStatus.OK);
    }

    @DeleteMapping("delete/{betId}")
    @Operation(summary = "Bet delete",
    description = "Allows user to delete his own bet")
    public ResponseEntity<MessageResponse> deleteBet(@PathVariable("betId") Long betId,
                                                     Principal principal){
        betService.deleteBet(betId, principal);
        return new ResponseEntity<>(new MessageResponse("Bet was deleted"), HttpStatus.OK);
    }
}
