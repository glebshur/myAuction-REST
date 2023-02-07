package com.example.my_auction_rest.dto;

import com.example.my_auction_rest.entity.enums.Role;
import com.example.my_auction_rest.validationGroup.OnUpdateByAdmin;
import com.example.my_auction_rest.validationGroup.OnUpdateByUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Schema(description = "User entity")
public class UserDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String email;

    @NotBlank(groups = OnUpdateByUser.class)
    @Schema(example = "username", minLength = 1, description = "Can be updated by user")
    private String username;

    @NotEmpty(groups = OnUpdateByAdmin.class)
    @Schema(description = "Can be updated by admin")
    private Set<Role> roles;

}
