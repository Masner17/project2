package com.masner.project2.dto.reservation;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequestDTO {

    @NotNull
    private LocalDateTime startDate;
    
    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Long userId;

    @NotNull
    private Long assetId;
}
