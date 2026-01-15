package com.masner.project2.mapper;

import com.masner.project2.dto.asset.AssetRequestDTO;
import com.masner.project2.dto.asset.AssetResponseDTO;
import com.masner.project2.entity.Asset;

public class AssetMapper {
    //DTO -> ENTITY
    public static Asset toEntity (AssetRequestDTO dto){
        Asset asset = new Asset();
        asset.setName(dto.getName());
        asset.setDescription(dto.getDescription());
        asset.setType(Asset.AssetType.valueOf(dto.getType()));

        return asset;
    }

    //ENTITY -> DTO
    public static AssetResponseDTO toResponse (Asset asset){
        AssetResponseDTO dto = new AssetResponseDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setDescription(asset.getDescription());
        dto.setType(asset.getType().name());
        dto.setActive((asset.isActive()));

        return dto;
    }
}
