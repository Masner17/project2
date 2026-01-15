package com.masner.project2.mapper;

import com.masner.project2.dto.reservation.ReservationRequestDTO;
import com.masner.project2.dto.reservation.ReservationResponseDTO;
import com.masner.project2.entity.Reservation;

public class ReservationMapper {

    public static Reservation toEntity (ReservationRequestDTO dto){
        Reservation reservation = new Reservation();
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

        return reservation;
    }

    public static ReservationResponseDTO toResponse(Reservation reservation){
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setStarDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setStatus(reservation.getStatus());
        dto.setUserId(reservation.getUser().getId());
        dto.setAssetId(reservation.getAsset().getId());

        return dto;
    }

}
