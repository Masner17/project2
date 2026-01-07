package com.masner.project2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masner.project2.entity.Asset;
import com.masner.project2.entity.Asset.AssetType;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByActiveTrue();

    
    // Buscar todos los assets por tipo (HOUSE, PARKING, etc.)
    List<Asset> findByType(AssetType type);

    Optional<Asset> findByName(String name);

}
