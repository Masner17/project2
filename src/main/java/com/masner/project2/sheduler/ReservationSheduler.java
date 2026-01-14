package com.masner.project2.sheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.masner.project2.service.ReservationService;

@Component
public class ReservationSheduler {

    private final ReservationService reservationService;

    public ReservationSheduler(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    //Se ejecuta cada un minuto
    @Scheduled(fixedRate = 60000)
    public void finalizeExpiredReservations(){
        reservationService.finalizeExpiredReservations();
    }
}
