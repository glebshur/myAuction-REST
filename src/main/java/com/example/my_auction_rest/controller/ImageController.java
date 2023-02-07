package com.example.my_auction_rest.controller;

import com.example.my_auction_rest.entity.Image;
import com.example.my_auction_rest.payload.response.MessageResponse;
import com.example.my_auction_rest.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
@Tag(name = "Image controller",
        description = "Controller allows to upload image to lot and to get lot's image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @PostMapping("/{lotId}/upload")
    @Operation(summary = "Image upload",
            description = "Allows owner to upload image to lot")
    public ResponseEntity<MessageResponse> uploadImageToLot(@RequestParam("file") MultipartFile file,
                                                            @PathVariable("lotId") Long lotId,
                                                            Principal principal) throws IOException {
        imageService.uploadImageToLot(file, principal, lotId);
        return new ResponseEntity<>(new MessageResponse("Image was uploaded successfully"), HttpStatus.OK);
    }

    @GetMapping("/{lotId}")
    @Operation(summary = "Get image",
            description = "Gives image to specific lot")
    public ResponseEntity<Image> getImageOfLot(@PathVariable("lotId") Long lotId) {
        Image image = imageService.getImageToLot(lotId);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
}
