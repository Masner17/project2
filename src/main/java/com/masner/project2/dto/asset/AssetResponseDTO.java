package com.masner.project2.dto.asset;

import lombok.Data;

@Data
public class AssetResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String type;
    private boolean active;

}
