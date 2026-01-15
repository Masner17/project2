package com.masner.project2.dto.reservation;

import java.time.LocalDateTime;

import com.masner.project2.entity.Reservation.Status;

import lombok.Data;

@Data
public class ReservationResponseDTO {

    private Long id;

    private LocalDateTime starDate;
    private LocalDateTime endDate;

    private Status status;

    private LocalDateTime createdAt;

    private Long userId;
    private Long assetId;


    public void setStatus(Status status) {
        this.status = status;
}
}
