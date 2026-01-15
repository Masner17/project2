package com.masner.project2.dto.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetRequestDTO {
    @NotBlank(message = "name required")
    private String name;

    private String description;

    @NotNull(message = "type required")
    private String type;

}
