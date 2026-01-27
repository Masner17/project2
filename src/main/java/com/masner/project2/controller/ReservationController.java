package com.masner.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masner.project2.dto.reservation.ReservationRequestDTO;
import com.masner.project2.dto.reservation.ReservationResponseDTO;
import com.masner.project2.entity.Reservation;
import com.masner.project2.mapper.ReservationMapper;
import com.masner.project2.service.ReservationService;


@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservation(){
        List<ReservationResponseDTO> reservation = reservationService.findAll()
            .stream()
            .map(ReservationMapper::toResponse)
            .toList();

        return ResponseEntity.ok(reservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservation(){
        List<ReservationResponseDTO> reservation = reservationService.findAllActive()
            .stream()
            .map(ReservationMapper::toResponse)
            .toList();

        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> saverReservation(@RequestBody ReservationRequestDTO request){
        try{
            Reservation reservation = ReservationMapper.toEntity(request);
            Reservation saved = reservationService.create(reservation, request.getUserId(), request.getAssetId());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservationMapper.toResponse(saved));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelReservation (@PathVariable Long id){
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/asset/{assetId}/availability")
    public ResponseEntity<List<ReservationResponseDTO>> getAssetAvailability(
            @PathVariable Long assetId) {

        List<ReservationResponseDTO> reservations = 
                reservationService.findActiveReservationsByAsset(assetId)
                .stream()
                .map(ReservationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(reservations);
    }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/finalize-expired")
    public ResponseEntity<String> expiredReservation (){
        reservationService.finalizeExpiredReservations();
        return ResponseEntity.ok("expired reservations finalized");
    }

}
