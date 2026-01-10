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

        //Obtener todas las reservas
    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

        //Obtener todas las disponibles
    public List<Reservation> findAllActive() {
        return reservationRepository.findByStatus(Reservation.Status.ACTIVE);
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
            throw new IllegalArgumentException("Asset is disabled");
         }
        if (startDate == null || endDate == null){
            throw new IllegalArgumentException("the date is required.");
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

    public void finalizeExpiredReservations(){
        List<Reservation> expiredReservations=
            reservationRepository.findByStatusAndEndDateBefore(
                Status.ACTIVE, LocalDateTime.now());

        for(Reservation reservation : expiredReservations){
            reservation.setStatus(Status.FINISHED);

        }

        reservationRepository.saveAll(expiredReservations);
    }

    public Reservation cancelReservation(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(()-> new IllegalArgumentException("Reservation not found"));
        

        //Cambiar el estado de la reserva
        reservation.setStatus(Status.CANCELLED);

        return reservationRepository.save(reservation);
    }
}

