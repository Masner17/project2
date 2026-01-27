package com.masner.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masner.project2.dto.asset.AssetRequestDTO;
import com.masner.project2.dto.asset.AssetResponseDTO;
import com.masner.project2.entity.Asset;
import com.masner.project2.mapper.AssetMapper;
import com.masner.project2.service.AssetService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService){
        this.assetService = assetService;
    }

    //stream lee cada asset .map lo convierte en assetResponseDTO
    //toList convierte el stream en una lista nuevamente
    @GetMapping
    public ResponseEntity<List<AssetResponseDTO>> getAllAssets(){
        List<AssetResponseDTO> assets = assetService.findAll()
            .stream()
            .map(AssetMapper::toResponse)
            .toList();
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AssetResponseDTO>> getActiveAssets(){
        List<AssetResponseDTO> assets = assetService.findAllActive()
            .stream()
            .map(AssetMapper::toResponse)
            .toList();

        return ResponseEntity.ok(assets);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AssetResponseDTO> saveAsset(@RequestBody AssetRequestDTO request){
            try {
                Asset asset = AssetMapper.toEntity(request);
                Asset saved = assetService.create(asset);
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AssetMapper.toResponse(saved));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> updateAsset(@PathVariable Long id, @RequestBody AssetRequestDTO request){
        Asset asset = AssetMapper.toEntity(request);
        Asset update = assetService.updateAsset(asset, id);
        return ResponseEntity.ok(AssetMapper.toResponse(update));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/desactivate")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id){
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Asset desactivate");
    }
}
