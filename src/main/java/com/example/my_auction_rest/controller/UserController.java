package com.example.my_auction_rest.controller;

import com.example.my_auction_rest.dto.UserDTO;
import com.example.my_auction_rest.entity.User;
import com.example.my_auction_rest.facade.UserFacade;
import com.example.my_auction_rest.service.UserService;
import com.example.my_auction_rest.validationGroup.OnUpdateByAdmin;
import com.example.my_auction_rest.validationGroup.OnUpdateByUser;
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
@CrossOrigin
@RequestMapping("api/user")
@Tag(name = "User controller",
        description = "Controller allows to get data about users and to update users")

public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/")
    @Operation(summary = "Info about current user",
            description = "Gives info about current user")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.convertUserToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{userId}")
    @Operation(summary = "Info about specific user",
            description = "Gives info about specific user by his id")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        UserDTO userDTO = userFacade.convertUserToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // Method that lets user update info about yourself
    @PutMapping("/update")
    @Operation(summary = "User update",
            description = "Allows user to update his username")
    public ResponseEntity<Object> updateUser(@Validated(OnUpdateByUser.class) @RequestBody UserDTO userDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User updatedUser = userService.updateUser(userDTO, principal);
        UserDTO updatedUserDTO = userFacade.convertUserToUserDTO(updatedUser);
        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }

    // Method that lets user with ROLE_ADMIN change roles of other users
    @Secured("ROLE_ADMIN")
    @PutMapping("/updateRoles/{userId}")
    @Operation(summary = "User's roles update",
            description = "Allows admin to update roles of specific user")
    public ResponseEntity<Object> updateUsersRoles(@PathVariable("userId") Long userId,
                                                   @Validated(OnUpdateByAdmin.class) @RequestBody UserDTO userDTO,
                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User updatedUser = userService.updateUser(userId, userDTO);
        UserDTO updatedUserDTO = userFacade.convertUserToUserDTO(updatedUser);
        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    @Operation(summary = "All users info",
            description = "Allows admin to show info about all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers().stream()
                .map(userFacade::convertUserToUserDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all/search")
    @Operation(summary = "User search",
            description = "Allows admin to search users by username")
    public ResponseEntity<List<UserDTO>> findUsers(@RequestParam("keyword") String keyword) {
        List<UserDTO> userDTOList = userService.getUsersByKeyword(keyword).stream()
                .map(userFacade::convertUserToUserDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }
}
