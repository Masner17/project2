package com.masner.project2.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masner.project2.entity.Asset;
import com.masner.project2.entity.Reservation;
import com.masner.project2.entity.Reservation.Status;
import com.masner.project2.entity.User;

public interface ReservationRepository extends JpaRepository <Reservation, Long> {

    //Traer todas las reservas de un usuario
    List<Reservation> findByUser(User user);

    //Traer todas las reservas de un asset
    List<Reservation> findByAsset(Asset asset);

    //Resesrvas de un asset que se solapen con un rango de fechas
    List<Reservation> findbyAssetAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        Asset asset, Date endDate, Date StartDate
    );
        /*
    SELECT *
        FROM reservations
        WHERE asset_id = ?
            AND start_date <= ?
            AND end_date >= ?
     */

    //Esto te trae todas las reservas ACTIVAS de ese asset.
    List<Reservation> findByAssetAndStatus(
        Asset asset,
        Status status
    );

    List<Reservation> findByStatusAndEndDateBefore(
    Status status,
    LocalDateTime date
);


}
