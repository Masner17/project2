package com.masner.project2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.masner.project2.entity.Asset;
import com.masner.project2.repository.AssetRepository;

@Service
public class AssetService {
    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository){
        this.assetRepository = assetRepository;
    }

    //Obtener todos los assets
    public List<Asset> findAll(){
        return assetRepository.findAll();
    }

    //Obtener todos los assets disponibles
    public List<Asset> findAllActive() {
        return assetRepository.findByActiveTrue();
    }

    //Crear
    public Asset create (Asset asset){
        validate(asset);
        return assetRepository.save(asset);
    }

    //Validaciones
    private void validate(Asset asset){

		    if (asset.getName() == null || asset.getName().isBlank()) {
			throw new IllegalArgumentException("The name is required");
		}
		    if (asset.getType() == null) { 
			throw new IllegalArgumentException("The type is required");
		}
       Optional<Asset> exist = assetRepository.findByName(asset.getName());
            if (exist.isPresent()) {
            throw new IllegalArgumentException("there is that name");
        }
         
    }

    //Actualizar
    public Asset updateAsset(Asset asset, Long id){

        Asset assetValidate = assetRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Asset does not exist"));

        validate(asset);
        assetValidate.setActive(asset.isActive());
        assetValidate.setDescription(asset.getDescription());
        assetValidate.setName(asset.getName());
        assetValidate.setType(asset.getType());

        return assetRepository.save(assetValidate);
    }

    //Borrar
    public void deleteAsset(Long id){
        Asset assetExist = assetRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("user does not exist"));
        
        assetRepository.deleteById(assetExist.getId());
        }
}
