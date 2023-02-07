package com.example.my_auction_rest.dto;

import com.example.my_auction_rest.entity.Lot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Schema(description = "Bet entity")
public class BetDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long lotId;

    @PositiveOrZero
    private BigDecimal amount;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;
}
