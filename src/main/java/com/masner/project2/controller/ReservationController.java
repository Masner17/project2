package com.masner.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masner.project2.entity.Reservation;
import com.masner.project2.service.ReservationService;


@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservation(){
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActiveReservation(){
        return ResponseEntity.ok(reservationService.findAllActive());
    }

    @PostMapping
    public ResponseEntity<Reservation> saverReservation(@RequestBody Reservation reservation){
        try{
            Reservation newReservation = reservationService.create(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(newReservation);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelReservation (@PathVariable Long id){
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled");
    }

    @GetMapping("/asset/{assetId}/availability")
    public ResponseEntity<List<Reservation>> getAssetAvailability(
            @PathVariable Long assetId) {

        return ResponseEntity.ok(
            reservationService.findActiveReservationsByAsset(assetId)
        );
    }

        @PutMapping("/finalize-expired")
    public ResponseEntity<String> expiredReservation (){
        reservationService.finalizeExpiredReservations();
        return ResponseEntity.ok("expired reservations finalized");
    }

}
