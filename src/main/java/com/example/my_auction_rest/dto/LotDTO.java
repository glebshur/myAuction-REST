package com.example.my_auction_rest.dto;

import com.example.my_auction_rest.validationGroup.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Lot entity")
public class LotDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Please, enter title", groups = OnUpdate.class)
    @Schema(example = "Title", minLength = 1)
    private String title;

    @NotBlank(message = "Please, enter description", groups = OnUpdate.class)
    @Schema(example = "Very short description", minLength = 1)
    private String description;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime startDate;

    @Future(message = "There must be a date in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "Please, enter end date")
    @Schema(example = "2023-01-11T11:36:00", description = "Lot closing date")
    private LocalDateTime endDate;

    @PositiveOrZero(message = "Cost cannot be less than zero")
    @Schema(minimum = "0")
    private BigDecimal initCost;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean active;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;
}
