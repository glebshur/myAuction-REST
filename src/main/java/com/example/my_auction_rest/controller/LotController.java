package com.example.my_auction_rest.controller;


import com.example.my_auction_rest.dto.LotDTO;
import com.example.my_auction_rest.entity.Lot;
import com.example.my_auction_rest.facade.LotFacade;
import com.example.my_auction_rest.payload.response.MessageResponse;
import com.example.my_auction_rest.service.LotService;
import com.example.my_auction_rest.validationGroup.OnUpdate;
import com.example.my_auction_rest.validator.ResponseErrorValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/lot")
@CrossOrigin
@Tag(name = "Lot controller",
        description = "Controller allows to create, update, delete, search and get info about lots")
public class LotController {

    @Autowired
    private LotFacade lotFacade;
    @Autowired
    private LotService lotService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @PostMapping("/create")
    @Operation(summary = "Lot creation",
            description = "Allows auctioneers to create lot")
    public ResponseEntity<Object> createLot(@Valid @RequestBody LotDTO lotDTO,
                                            BindingResult bindingResult,
                                            Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Lot lot = lotService.createLot(lotDTO, principal);
        LotDTO createdLot = lotFacade.convertLotToLotDTO(lot);

        return new ResponseEntity<>(createdLot, HttpStatus.OK);
    }

    @GetMapping("/{lotId}")
    @Operation(summary = "Lot's info",
            description = "Gives info about specific lot")
    public ResponseEntity<LotDTO> getLot(@PathVariable("lotId") Long lotId) {
        Lot lot = lotService.getLotById(lotId);
        LotDTO lotDTO = lotFacade.convertLotToLotDTO(lot);

        return new ResponseEntity<>(lotDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "All lots info",
            description = "Gives info about all lots")
    public ResponseEntity<List<LotDTO>> getAllLots() {
        List<LotDTO> lots = lotService.getAllLots(true).stream()
                .map(lotFacade::convertLotToLotDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(lots, HttpStatus.OK);
    }

    @GetMapping("/all/search")
    @Operation(summary = "Lot search",
            description = "Allows user to search lots by keyword and activity")
    public ResponseEntity<List<LotDTO>> findAllLots(@RequestParam("keyword") String keyword,
                                                    @RequestParam("active") boolean active) {
        List<LotDTO> lots = lotService.getLotsByKeywordAndActivity(keyword, active).stream()
                .map(lotFacade::convertLotToLotDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(lots, HttpStatus.OK);
    }

    @GetMapping("/all/currentUser")
    @Operation(summary = "User's lots info",
            description = "Gives info about all lots created by user")
    public ResponseEntity<List<LotDTO>> getAllLotsForUser(@RequestParam("active") boolean active,
                                                          Principal principal) {
        List<LotDTO> lots = lotService.getAllLotsForUser(principal, active).stream()
                .map(lotFacade::convertLotToLotDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(lots, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @PutMapping("/update/{lotId}")
    @Operation(summary = "Lot update",
            description = "Allows auctioneer to update lot's info")
    public ResponseEntity<Object> updateLot(@PathVariable("lotId") Long lotId,
                                            @Validated(OnUpdate.class) @RequestBody LotDTO lotDTO,
                                            BindingResult bindingResult,
                                            Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Lot lot = lotService.updateLot(lotId, lotDTO, principal);
        LotDTO updatedLot = lotFacade.convertLotToLotDTO(lot);
        return new ResponseEntity<>(updatedLot, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @DeleteMapping("delete/{lotId}")
    @Operation(summary = "Lot delete",
            description = "Allows auctioneers to delete their own lots")
    public ResponseEntity<MessageResponse> deleteLot(@PathVariable("lotId") Long lotId,
                                                     Principal principal) {
        lotService.deleteLot(lotId, principal);
        return new ResponseEntity<>(new MessageResponse("Lot was deleted"), HttpStatus.OK);
    }
}
