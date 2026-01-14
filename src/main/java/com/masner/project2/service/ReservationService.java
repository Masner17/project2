package com.masner.project2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import com.masner.project2.entity.Asset;
import com.masner.project2.entity.Reservation;
import com.masner.project2.entity.Reservation.Status;
import com.masner.project2.entity.User;
import com.masner.project2.repository.AssetRepository;
import com.masner.project2.repository.ReservationRepository;
import com.masner.project2.repository.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository,
        AssetRepository assetRepository){

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
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
    User user = userRepository.findById(reservation.getUser().getId())
        .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

    Asset asset = assetRepository.findById(reservation.getAsset().getId())
        .orElseThrow(() -> new IllegalArgumentException("Asset does not exist"));

    //reemplazamos
    reservation.setUser(user);
    reservation.setAsset(asset);

    validateReservation(reservation);

    //estado inicial
    reservation.setStatus(Reservation.Status.ACTIVE);

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


        //Buscar reservas activas del mismo asset
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

    public List<Reservation> findActiveReservationsByAsset(Long assetId) {

    Asset asset = assetRepository.findById(assetId)
        .orElseThrow(() -> new IllegalArgumentException("Asset does not exist"));

    if (!asset.isActive()) {
        throw new IllegalArgumentException("Asset is disabled");
    }

    return reservationRepository.findByAssetAndStatus(
        asset,
        Reservation.Status.ACTIVE
    );
}
}

