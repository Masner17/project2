package com.masner.project2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.masner.project2.entity.Asset;
import com.masner.project2.entity.Reservation;
import com.masner.project2.entity.Reservation.Status;
import com.masner.project2.entity.User;
import com.masner.project2.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

        //Obtener todos los usuarios
    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    //Crear reserva
    public Reservation create (Reservation reservation){
        validateReservation(reservation);
        return reservationRepository.save(reservation);
    }

    private void validateReservation( Reservation reservation){
       User userActive = reservation.getUser();
       Asset assetActive = reservation.getAsset();
       LocalDateTime startDate = reservation.getStartDate();
       LocalDateTime endDate = reservation.getEndDate();

        if (!userActive.isActive()) { 
            throw new IllegalArgumentException("user inactive");
         }  
        if (!assetActive.isActive()) {
            throw new IllegalArgumentException("asset inactive");
         }  
        
		if (reservation.getStartDate() == null) {
			throw new IllegalArgumentException("The date is required");
		}
		if (reservation.getEndDate() == null) {
			throw new IllegalArgumentException("The date is required");
		}  
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Error date. end date later that star date");
		} 
        if (startDate.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("The reservation must be for a date later than the date of the reservation.");
        } 

        // 2️⃣ Buscar reservas activas del mismo asset
        List<Reservation> activeReservations =
            reservationRepository.findByAssetAndStatus(assetActive, Status.ACTIVE);

        for (Reservation existing : activeReservations) {

        boolean overlap =
                startDate.isBefore(existing.getEndDate()) &&
                endDate.isAfter(existing.getStartDate());

        if (overlap) {
            throw new IllegalArgumentException(
                "The asset is already reserved in that time range"
            );
        }
    }

        
        reservation.setStatus(Status.ACTIVE);
        reservation.setCreatedAt(LocalDateTime.now());
    }
    
}
