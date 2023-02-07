package com.example.my_auction_rest.facade;

import com.example.my_auction_rest.dto.UserDTO;
import com.example.my_auction_rest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO convertUserToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }
}
