package com.example.my_auction_rest.controller;

import com.example.my_auction_rest.dto.BetDTO;
import com.example.my_auction_rest.entity.WinningBet;
import com.example.my_auction_rest.facade.BetFacade;
import com.example.my_auction_rest.service.WinningBetService;
import com.example.my_auction_rest.validator.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/winningBet")
@CrossOrigin
@Tag(name = "Winning bet controller",
        description = "Controller allows you to get all winnings bets of user")
public class WinningBetController {

    @Autowired
    private BetFacade betFacade;
    @Autowired
    private WinningBetService winningBetService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/all")
    @Operation(summary = "Winning bets info",
            description = "Gives all winning bets of current user")
    public ResponseEntity<List<BetDTO>> getAllWinningBetsForUser(Principal principal) {
        List<BetDTO> bets = winningBetService.getAllWinningBetsForUser(principal).stream()
                .map(winningBet -> betFacade.convertBetToBetDTO(winningBet.getBet()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(bets, HttpStatus.OK);
    }
}
